package com.corales.webdev2;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:week8-pet-tests;DB_CLOSE_DELAY=-1",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.show-sql=false"
})
@AutoConfigureMockMvc
class PetIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private PetService petService;

    @BeforeEach
    void resetPets() {
        petRepository.deleteAll();
        petRepository.save(new Pet("CHIP-0001", "Luna", "Cat", "Siamese"));
        petRepository.flush();
    }

    @Test
    void seedAndCrudEndpointsReturnExpectedResponses() throws Exception {
        mockMvc.perform(get("/api/pets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].microchipNumber").value("CHIP-0001"));

        mockMvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"microchipNumber\":\"CHIP-0002\",\"name\":\"Buddy\",\"species\":\"Dog\",\"breed\":\"Beagle\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber());

        Pet buddy = petRepository.findByNameContainingIgnoreCase("buddy").get(0);
        mockMvc.perform(get("/api/pets/{id}", buddy.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Buddy"));

        mockMvc.perform(put("/api/pets/{id}", buddy.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"microchipNumber\":\"CHIP-0002\",\"name\":\"Buddy Updated\",\"species\":\"Dog\",\"breed\":\"Beagle\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Buddy Updated"));

        mockMvc.perform(delete("/api/pets/{id}", buddy.getId()))
                .andExpect(status().isNoContent());
        assertThat(petRepository.findById(buddy.getId())).isEmpty();
    }

    @Test
    void derivedAndCustomQueriesReturnFilteredResults() throws Exception {
        petRepository.save(new Pet("CHIP-0002", "Bingo", "Dog", "Beagle"));
        petRepository.save(new Pet("CHIP-0003", "Luna Two", "Cat", "Persian"));

        mockMvc.perform(get("/api/pets/species").param("name", "cat"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].species").value(org.hamcrest.Matchers.everyItem(org.hamcrest.Matchers.is("Cat"))));
        mockMvc.perform(get("/api/pets/search").param("keyword", "lun"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").exists());
        mockMvc.perform(get("/api/pets/species-list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", org.hamcrest.Matchers.containsInAnyOrder("Cat", "Dog")));
    }

    @Test
    void batchRollbackDoesNotLeavePartialWrite() {
        long countBefore = petRepository.count();
        Pet first = new Pet("CHIP-BATCH-NEW", "Batch Pet", "Dog", "Poodle");
        Pet duplicate = new Pet("CHIP-0001", "Duplicate", "Cat", "Mixed");

        assertThatThrownBy(() -> petService.batchRegister(List.of(first, duplicate)))
                .isInstanceOf(DataIntegrityViolationException.class);

        assertThat(petRepository.count()).isEqualTo(countBefore);
        assertThat(petRepository.findByNameContainingIgnoreCase("Batch Pet")).isEmpty();
    }

    @Test
    void missingIdAndInvalidPetHaveExpectedResponses() throws Exception {
        mockMvc.perform(get("/api/pets/{id}", 999999L))
                .andExpect(status().isNotFound());
        mockMvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"microchipNumber\":null,\"name\":\"Bad\",\"species\":\"Dog\",\"breed\":\"Mixed\"}"))
                .andExpect(status().isBadRequest());
    }
}
