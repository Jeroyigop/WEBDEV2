package com.corales.webdev2;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {

    boolean existsByMicrochipNumber(String microchipNumber);

    boolean existsByMicrochipNumberAndIdNot(String microchipNumber, Long id);
}
