package com.corales.webdev2;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
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
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(0))
                .andExpect(jsonPath("$.totalElements").value(0));

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
                .andExpect(jsonPath("$.content[0].microchipNumber").value("CHIP-W10-001"));
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
        mockMvc.perform(get("/api/pets/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value(containsString("not found")))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.errors").value(nullValue()));
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
                .andExpect(jsonPath("$.message").value(containsString("microchip")))
                .andExpect(jsonPath("$.status").value(409));
        mockMvc.perform(post("/api/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"microchipNumber":"","name":"Milo","species":"Cat","breed":"Domestic"}
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors[0].field").value("microchipNumber"))
                .andExpect(jsonPath("$.errors[0].message").value(containsString("required")));
    }

    @Test
    void listSupportsPaginationFilteringAndCapsPageSize() throws Exception {
        petRepository.saveAll(java.util.List.of(
                new Pet("CHIP-W11-001", "Clover", "Rabbit", "Holland Lop"),
                new Pet("CHIP-W11-002", "Milo", "Cat", "Domestic"),
                new Pet("CHIP-W11-003", "Poppy", "Rabbit", "Lionhead"),
                new Pet("CHIP-W11-004", "Daisy", "Dog", "Beagle"),
                new Pet("CHIP-W11-005", "Ollie", "Cat", "Tabby"),
                new Pet("CHIP-W11-006", "Luna", "Rabbit", "Mini Rex"),
                new Pet("CHIP-W11-007", "Scout", "Dog", "Collie")));

        mockMvc.perform(get("/api/pets?page=0&size=2&sort=name,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].name").value("Clover"))
                .andExpect(jsonPath("$.totalElements").value(7))
                .andExpect(jsonPath("$.totalPages").value(4))
                .andExpect(jsonPath("$.size").value(2));

        mockMvc.perform(get("/api/pets?species=Rabbit"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.content[0].species").value("Rabbit"));

        mockMvc.perform(get("/api/pets?size=999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(100))
                .andExpect(jsonPath("$.totalElements").value(7));
    }

    @Test
    void corsAllowsConfiguredFrontendOrigin() throws Exception {
        mockMvc.perform(options("/api/pets")
                .header("Origin", "http://localhost:3000")
                .header("Access-Control-Request-Method", "POST"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:3000"))
                .andExpect(header().string("Access-Control-Allow-Credentials", "true"))
                .andExpect(header().string("Access-Control-Allow-Methods", containsString("POST")));
    }

    @Test
    void invalidPaginationReturnsFieldLevelBadRequest() throws Exception {
        mockMvc.perform(get("/api/pets?page=-1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors[0].field").value("page"));
    }
}
