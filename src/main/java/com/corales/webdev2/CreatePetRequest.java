package com.corales.webdev2;

import jakarta.validation.constraints.NotBlank;

public record CreatePetRequest(
        @NotBlank String microchipNumber,
        @NotBlank String name,
        @NotBlank String species,
        @NotBlank String breed) {
}
