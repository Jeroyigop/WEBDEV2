package com.corales.webdev2;

import jakarta.validation.constraints.Size;

public record UpdatePetRequest(
        @Size(max = 50, message = "Microchip number must be at most 50 characters.")
        String microchipNumber,
        @Size(max = 100, message = "Pet name must be at most 100 characters.")
        String name,
        @Size(max = 80, message = "Species must be at most 80 characters.")
        String species,
        @Size(max = 100, message = "Breed must be at most 100 characters.")
        String breed) {
}
