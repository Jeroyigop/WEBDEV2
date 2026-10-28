package com.corales.webdev2;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pets")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @PostMapping
    public ResponseEntity<Pet> register(@RequestBody Pet pet) {
        Pet saved = petService.registerPet(pet);
        return ResponseEntity.created(URI.create("/api/pets/" + saved.getId())).body(saved);
    }

    @GetMapping
    public List<Pet> getAll() {
        return petService.getAllPets();
    }

    @GetMapping("/{id}")
    public Pet getById(@PathVariable Long id) {
        return petService.getPetById(id);
    }

    @PutMapping("/{id}")
    public Pet update(@PathVariable Long id, @RequestBody Pet pet) {
        return petService.updatePet(id, pet);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        petService.removePet(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/batch")
    public ResponseEntity<Void> batch(@RequestBody List<Pet> pets) {
        petService.batchRegister(pets);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/species")
    public List<Pet> findBySpecies(@RequestParam("name") String species) {
        return petService.findBySpecies(species);
    }

    @GetMapping("/search")
    public List<Pet> searchByName(@RequestParam String keyword) {
        return petService.searchByName(keyword);
    }

    @GetMapping("/species-list")
    public List<String> getSpecies() {
        return petService.getAllSpecies();
    }
}
