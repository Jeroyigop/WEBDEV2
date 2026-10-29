package com.corales.webdev2;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PetRepository extends JpaRepository<Pet, Long> {

    List<Pet> findBySpeciesIgnoreCase(String species);

    List<Pet> findByNameContainingIgnoreCase(String keyword);

    @Query("SELECT DISTINCT p.species FROM Pet p")
    List<String> findAllSpecies();

    @Query("SELECT p FROM Pet p LEFT JOIN FETCH p.owner WHERE p.id = :id")
    java.util.Optional<Pet> findWithOwnerById(@Param("id") Long id);
}
