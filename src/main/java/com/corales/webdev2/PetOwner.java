package com.corales.webdev2;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "pet_owners")
public class PetOwner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    @OneToMany(mappedBy = "owner", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JsonIgnore
    private List<Pet> pets = new ArrayList<>();

    public PetOwner() {
    }

    public PetOwner(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public void addPet(Pet pet) {
        if (pet == null || pets.contains(pet)) {
            return;
        }
        pets.add(pet);
        pet.setOwner(this);
    }

    public void removePet(Pet pet) {
        if (pets.remove(pet)) {
            pet.setOwner(null);
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public List<Pet> getPets() {
        return pets;
    }
}
