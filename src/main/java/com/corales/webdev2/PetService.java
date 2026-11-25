package com.corales.webdev2;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
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
    public List<PetResponse> findAll() {
        return petRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public Optional<PetResponse> findById(Long id) {
        return petRepository.findById(id).map(this::toResponse);
    }

    public PetResponse create(CreatePetRequest request) {
        if (petRepository.existsByMicrochipNumber(request.microchipNumber())) {
            throw new PetConflictException("A pet with that microchip number already exists.");
        }

        Pet pet = new Pet(request.microchipNumber(), request.name(), request.species(), request.breed());
        return toResponse(save(pet));
    }

    public Optional<PetResponse> replace(Long id, UpdatePetRequest request) {
        validateCompleteRequest(request);
        Optional<Pet> found = petRepository.findById(id);
        if (found.isEmpty()) {
            return Optional.empty();
        }

        Pet pet = found.get();
        ensureMicrochipAvailable(request.microchipNumber(), id);
        pet.setMicrochipNumber(request.microchipNumber());
        pet.setName(request.name());
        pet.setSpecies(request.species());
        pet.setBreed(request.breed());
        return Optional.of(toResponse(save(pet)));
    }

    public Optional<PetResponse> patch(Long id, UpdatePetRequest request) {
        Optional<Pet> found = petRepository.findById(id);
        if (found.isEmpty()) {
            return Optional.empty();
        }

        Pet pet = found.get();
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
        return Optional.of(toResponse(save(pet)));
    }

    public boolean delete(Long id) {
        if (!petRepository.existsById(id)) {
            return false;
        }
        petRepository.deleteById(id);
        return true;
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
            throw new PetRequestValidationException(field + " must not be blank.");
        }
    }

    private void ensureMicrochipAvailable(String microchipNumber, Long petId) {
        if (petRepository.existsByMicrochipNumberAndIdNot(microchipNumber, petId)) {
            throw new PetConflictException("A pet with that microchip number already exists.");
        }
    }
}
