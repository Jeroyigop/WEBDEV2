package com.corales.webdev2;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {

    boolean existsByMicrochipNumber(String microchipNumber);

    boolean existsByMicrochipNumberAndIdNot(String microchipNumber, Long id);

    Page<Pet> findBySpeciesIgnoreCase(String species, Pageable pageable);
}
