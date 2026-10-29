package com.corales.webdev2;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface PetTagRepository extends JpaRepository<PetTag, Long> {

    @Query("SELECT pt FROM PetTag pt JOIN FETCH pt.pet JOIN FETCH pt.tag WHERE pt.id = :id")
    Optional<PetTag> findWithPetAndTagById(@Param("id") Long id);
}
