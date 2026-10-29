package com.corales.webdev2;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import java.util.Optional;

public interface PetOwnerRepository extends JpaRepository<PetOwner, Long> {

    @EntityGraph(attributePaths = "pets")
    Optional<PetOwner> findWithPetsById(Long id);
}
