package com.corales.webdev2;

public record UpdatePetRequest(String microchipNumber, String name, String species, String breed) {
}
