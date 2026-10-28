package com.corales.webdev2;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PetService {

    private final PetRepository petRepository;
    private final EntityManager entityManager;

    public PetService(PetRepository petRepository, EntityManager entityManager) {
        this.petRepository = petRepository;
        this.entityManager = entityManager;
    }

    public Pet registerPet(Pet pet) {
        validate(pet);
        return petRepository.save(pet);
    }

    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    public Pet getPetById(Long id) {
        return petRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pet not found: " + id));
    }

    public Pet updatePet(Long id, Pet updated) {
        validate(updated);
        Pet pet = getPetById(id);
        pet.setMicrochipNumber(updated.getMicrochipNumber());
        pet.setName(updated.getName());
        pet.setSpecies(updated.getSpecies());
        pet.setBreed(updated.getBreed());
        return petRepository.save(pet);
    }

    public void removePet(Long id) {
        Pet pet = getPetById(id);
        petRepository.delete(pet);
    }

    public List<Pet> findBySpecies(String species) {
        return petRepository.findBySpeciesIgnoreCase(species);
    }

    public List<Pet> searchByName(String keyword) {
        return petRepository.findByNameContainingIgnoreCase(keyword);
    }

    public List<String> getAllSpecies() {
        return petRepository.findAllSpecies();
    }

    @Transactional
    public void batchRegister(List<Pet> pets) {
        for (Pet pet : pets) {
            validate(pet);
            petRepository.save(pet);
            entityManager.flush();
        }
    }

    private void validate(Pet pet) {
        if (pet == null
                || pet.getMicrochipNumber() == null
                || pet.getMicrochipNumber().isBlank()
                || pet.getName() == null
                || pet.getName().isBlank()
                || pet.getSpecies() == null
                || pet.getSpecies().isBlank()
                || pet.getBreed() == null
                || pet.getBreed().isBlank()) {
            throw new IllegalArgumentException("All pet fields except id are required.");
        }
    }
}
