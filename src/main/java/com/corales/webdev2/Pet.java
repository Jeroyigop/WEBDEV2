package com.corales.webdev2;

import jakarta.persistence.Column;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pet")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "microchip_number", unique = true, nullable = false)
    @NotBlank
    private String microchipNumber;

    @Column(nullable = false)
    @NotBlank
    private String name;

    @Column(nullable = false)
    @NotBlank
    private String species;

    @Column(nullable = false)
    @NotBlank
    private String breed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    @JsonIgnore
    private PetOwner owner;

    @OneToMany(mappedBy = "pet", cascade = { CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REMOVE }, orphanRemoval = true)
    @JsonIgnore
    private List<PetTag> tagAssignments = new ArrayList<>();

    public Pet() {
    }

    public Pet(String microchipNumber, String name, String species, String breed) {
        this.microchipNumber = microchipNumber;
        this.name = name;
        this.species = species;
        this.breed = breed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMicrochipNumber() {
        return microchipNumber;
    }

    public void setMicrochipNumber(String microchipNumber) {
        this.microchipNumber = microchipNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public PetOwner getOwner() {
        return owner;
    }

    void setOwner(PetOwner owner) {
        this.owner = owner;
    }

    public PetTag addTag(Tag tag, String appliedOn, String notes) {
        PetTag assignment = new PetTag(this, tag, appliedOn, notes);
        tagAssignments.add(assignment);
        tag.addPetTag(assignment);
        return assignment;
    }

    public List<PetTag> getTagAssignments() {
        return tagAssignments;
    }
}
