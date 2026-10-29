package com.corales.webdev2;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "pet_tag_assignments", uniqueConstraints = @UniqueConstraint(name = "uk_pet_tag",
        columnNames = { "pet_id", "tag_id" }))
public class PetTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "applied_on", nullable = false)
    private LocalDate appliedOn;

    private String notes;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pet_id", nullable = false)
    @JsonIgnore
    private Pet pet;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tag_id", nullable = false)
    @JsonIgnore
    private Tag tag;

    public PetTag() {
    }

    public PetTag(Pet pet, Tag tag, String appliedOn, String notes) {
        this.pet = pet;
        this.tag = tag;
        this.appliedOn = LocalDate.parse(appliedOn);
        this.notes = notes;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getAppliedOn() {
        return appliedOn;
    }

    public String getNotes() {
        return notes;
    }

    public Pet getPet() {
        return pet;
    }

    public Tag getTag() {
        return tag;
    }
}
