package com.corales.webdev2;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PetService {

    private final PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @Transactional(readOnly = true)
    public Page<PetResponse> findAll(int page, int size, String sort, String direction, String species) {
        if (page < 0) {
            throw new PetRequestValidationException("page", "Page must be zero or greater.");
        }
        if (size < 1) {
            throw new PetRequestValidationException("size", "Page size must be at least 1.");
        }
        String sortField = sort;
        String sortDirectionValue = direction;
        if (sort.contains(",")) {
            String[] sortParts = sort.split(",", -1);
            if (sortParts.length != 2 || sortParts[0].isBlank() || sortParts[1].isBlank()) {
                throw new PetRequestValidationException("sort", "Sort must use the format field,direction.");
            }
            sortField = sortParts[0].trim();
            sortDirectionValue = sortParts[1].trim();
        }
        if (!ListSortField.isSupported(sortField)) {
            throw new PetRequestValidationException("sort", "Sort must be one of id, microchipNumber, name, species, or breed.");
        }

        Sort.Direction sortDirection;
        try {
            sortDirection = Sort.Direction.fromString(sortDirectionValue);
        } catch (IllegalArgumentException exception) {
            throw new PetRequestValidationException("sort", "Sort direction must be asc or desc.");
        }

        Pageable pageable = PageRequest.of(page, Math.min(size, 100),
                Sort.by(sortDirection, sortField));
        Page<Pet> pets = species == null || species.isBlank()
                ? petRepository.findAll(pageable)
                : petRepository.findBySpeciesIgnoreCase(species.trim(), pageable);
        return pets.map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public PetResponse findById(Long id) {
        return petRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Pet with ID " + id + " was not found."));
    }

    public PetResponse create(CreatePetRequest request) {
        if (petRepository.existsByMicrochipNumber(request.microchipNumber())) {
            throw new PetConflictException("A pet with that microchip number already exists.");
        }

        Pet pet = new Pet(request.microchipNumber(), request.name(), request.species(), request.breed());
        return toResponse(save(pet));
    }

    public PetResponse replace(Long id, UpdatePetRequest request) {
        validateCompleteRequest(request);
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pet with ID " + id + " was not found."));
        ensureMicrochipAvailable(request.microchipNumber(), id);
        pet.setMicrochipNumber(request.microchipNumber());
        pet.setName(request.name());
        pet.setSpecies(request.species());
        pet.setBreed(request.breed());
        return toResponse(save(pet));
    }

    public PetResponse patch(Long id, UpdatePetRequest request) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pet with ID " + id + " was not found."));
        if (request.microchipNumber() != null) {
            validateText(request.microchipNumber(), "microchipNumber");
            ensureMicrochipAvailable(request.microchipNumber(), id);
            pet.setMicrochipNumber(request.microchipNumber());
        }
        if (request.name() != null) {
            validateText(request.name(), "name");
            pet.setName(request.name());
        }
        if (request.species() != null) {
            validateText(request.species(), "species");
            pet.setSpecies(request.species());
        }
        if (request.breed() != null) {
            validateText(request.breed(), "breed");
            pet.setBreed(request.breed());
        }
        return toResponse(save(pet));
    }

    public void delete(Long id) {
        if (!petRepository.existsById(id)) {
            throw new ResourceNotFoundException("Pet with ID " + id + " was not found.");
        }
        petRepository.deleteById(id);
    }

    private PetResponse toResponse(Pet pet) {
        return new PetResponse(pet.getId(), pet.getMicrochipNumber(), pet.getName(), pet.getSpecies(), pet.getBreed());
    }

    private Pet save(Pet pet) {
        try {
            return petRepository.saveAndFlush(pet);
        } catch (DataIntegrityViolationException exception) {
            throw new PetConflictException("A pet conflicts with an existing microchip number.");
        }
    }

    private void validateCompleteRequest(UpdatePetRequest request) {
        validateText(request.microchipNumber(), "microchipNumber");
        validateText(request.name(), "name");
        validateText(request.species(), "species");
        validateText(request.breed(), "breed");
    }

    private void validateText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new PetRequestValidationException(field, field + " must not be blank.");
        }
    }

    private void ensureMicrochipAvailable(String microchipNumber, Long petId) {
        if (petRepository.existsByMicrochipNumberAndIdNot(microchipNumber, petId)) {
            throw new PetConflictException("A pet with that microchip number already exists.");
        }
    }

    private enum ListSortField {
        ID("id"),
        MICROCHIP_NUMBER("microchipNumber"),
        NAME("name"),
        SPECIES("species"),
        BREED("breed");

        private final String property;

        ListSortField(String property) {
            this.property = property;
        }

        private static boolean isSupported(String value) {
            for (ListSortField field : values()) {
                if (field.property.equals(value)) {
                    return true;
                }
            }
            return false;
        }
    }
}
