package com.corales.webdev2;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreatePetRequest(
        @NotBlank(message = "Microchip number is required.")
        @Size(max = 50, message = "Microchip number must be at most 50 characters.")
        String microchipNumber,
        @NotBlank(message = "Pet name is required.")
        @Size(max = 100, message = "Pet name must be at most 100 characters.")
        String name,
        @NotBlank(message = "Species is required.")
        @Size(max = 80, message = "Species must be at most 80 characters.")
        String species,
        @NotBlank(message = "Breed is required.")
        @Size(max = 100, message = "Breed must be at most 100 characters.")
        String breed) {
}
