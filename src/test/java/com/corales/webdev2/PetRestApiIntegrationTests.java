package com.corales.webdev2;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:week10-pet-api;DB_CLOSE_DELAY=-1",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.show-sql=false"
})
@AutoConfigureMockMvc
class PetRestApiIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PetRepository petRepository;

    @BeforeEach
    void clearPets() {
        petRepository.deleteAll();
    }

    @Test
    void completeCrudWorkflowReturnsExpectedStatusesAndLocation() throws Exception {
        mockMvc.perform(get("/api/pets"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        MvcResult created = mockMvc.perform(post("/api/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"microchipNumber":"CHIP-W10-001","name":"Clover","species":"Rabbit","breed":"Holland Lop"}
                        """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/pets/")))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Clover"))
                .andReturn();

        String location = created.getResponse().getHeader("Location");
        String id = location.substring(location.lastIndexOf('/') + 1);
        mockMvc.perform(get("/api/pets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].microchipNumber").value("CHIP-W10-001"));
        mockMvc.perform(get("/api/pets/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Clover"));
        mockMvc.perform(put("/api/pets/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"microchipNumber":"CHIP-W10-001","name":"Clover Updated","species":"Rabbit","breed":"Mini Lop"}
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Clover Updated"))
                .andExpect(jsonPath("$.breed").value("Mini Lop"));
        mockMvc.perform(patch("/api/pets/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"name":"Clover Patched"}
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Clover Patched"))
                .andExpect(jsonPath("$.species").value("Rabbit"))
                .andExpect(jsonPath("$.breed").value("Mini Lop"));
        mockMvc.perform(delete("/api/pets/{id}", id))
                .andExpect(status().isNoContent());
        mockMvc.perform(delete("/api/pets/{id}", id))
                .andExpect(status().isNotFound());
        mockMvc.perform(get("/api/pets/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void duplicateMicrochipReturnsConflictAndInvalidCreateReturnsBadRequest() throws Exception {
        String request = """
                {"microchipNumber":"CHIP-W10-DUP","name":"Milo","species":"Cat","breed":"Domestic"}
                """;
        mockMvc.perform(post("/api/pets").contentType(MediaType.APPLICATION_JSON).content(request))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/api/pets").contentType(MediaType.APPLICATION_JSON).content(request))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(containsString("microchip")));
        mockMvc.perform(post("/api/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"microchipNumber":"","name":"Milo","species":"Cat","breed":"Domestic"}
                        """))
                .andExpect(status().isBadRequest());
    }
}
