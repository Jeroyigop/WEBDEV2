package com.corales.webdev2;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "pet_tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "tag", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<PetTag> petTags = new ArrayList<>();

    public Tag() {
    }

    public Tag(String name) {
        this.name = name;
    }

    void addPetTag(PetTag petTag) {
        if (!petTags.contains(petTag)) {
            petTags.add(petTag);
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<PetTag> getPetTags() {
        return petTags;
    }
}
