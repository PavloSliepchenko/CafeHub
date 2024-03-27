package com.example.cafehub.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.cafehub.dto.cafe.CafeResponseDto;
import com.example.cafehub.dto.cafe.CreateCafeDto;
import com.example.cafehub.model.Cafe;
import com.example.cafehub.repository.CafeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CafeControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CafeRepository cafeRepository;

    @BeforeAll
    static void setUp(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test()
    @DisplayName("Get cafe by name")
    void getCafeByName_ValidRequest_ShouldReturnListOfCafeDtos() throws Exception {
        String name = "  blur  ";
        String city = "Kyiv";
        MvcResult result =
                mockMvc.perform(get(String.format("/cafes/name?name=%s&city=%s", name, city)))
                        .andExpect(status().isOk())
                        .andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        List<CafeResponseDto> actual =
                List.of(objectMapper.readValue(contentAsString, CafeResponseDto[].class));
        Assertions.assertEquals(1, actual.size());
        Assertions.assertEquals(2L, actual.get(0).getId());
        Assertions.assertEquals("Blur", actual.get(0).getName());

        name = " ne";
        result = mockMvc.perform(get(String.format("/cafes/name?name=%s&city=%s", name, city)))
                .andExpect(status().isOk())
                .andReturn();
        contentAsString = result.getResponse().getContentAsString();
        actual = List.of(objectMapper.readValue(contentAsString, CafeResponseDto[].class));
        Assertions.assertEquals(3, actual.size());
        Assertions.assertEquals("One Love", actual.get(0).getName());

        name = "o";
        result = mockMvc.perform(get(String.format("/cafes/name?name=%s&city=%s", name, city)))
                .andExpect(status().isOk())
                .andReturn();
        contentAsString = result.getResponse().getContentAsString();
        actual = List.of(objectMapper.readValue(contentAsString, CafeResponseDto[].class));
        Assertions.assertEquals(16, actual.size());
    }

    @Test
    @DisplayName("Get all cafes")
    void getAllCafes_ValidRequest_ShouldReturnListOfCafeDtos() throws Exception {
        MvcResult result = mockMvc.perform(get("/cafes?page=0&size=30"))
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        List<CafeResponseDto> actual =
                List.of(objectMapper.readValue(contentAsString, CafeResponseDto[].class));
        Assertions.assertEquals(25, actual.size());
        for (CafeResponseDto cafe : actual) {
            Assertions.assertNotNull(cafe);
            assertThat(cafe.getId()).isIn(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L, 12L, 13L,
                    14L, 15L, 16L, 17L, 18L, 19L, 20L, 21L, 22L, 27L, 23L, 24L, 25L);
        }
    }

    @Test
    @DisplayName("Get all cafes in the city")
    void getAllCafesInCity_ValidRequest_ShouldReturnListOfCafeDtos() throws Exception {
        String city = "Kyiv";
        MvcResult result =
                mockMvc.perform(get(String.format("/cafes/city?page=0&size=30&city=%s", city)))
                        .andExpect(status().isOk())
                        .andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        List<CafeResponseDto> actual =
                List.of(objectMapper.readValue(contentAsString, CafeResponseDto[].class));
        Assertions.assertEquals(25, actual.size());
        for (CafeResponseDto cafe : actual) {
            Assertions.assertNotNull(cafe);
            assertThat(cafe.getId()).isIn(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L, 12L, 13L,
                    14L, 15L, 16L, 17L, 18L, 19L, 20L, 21L, 22L, 27L, 23L, 24L, 25L);
        }

        city = "Lviv";
        result = mockMvc.perform(get(String.format("/cafes/city?page=0&size=30&city=%s", city)))
                .andExpect(status().isOk())
                .andReturn();
        contentAsString = result.getResponse().getContentAsString();
        actual = List.of(objectMapper.readValue(contentAsString, CafeResponseDto[].class));
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    @DisplayName("Find cafe by id")
    void getCafeById_ValidRequest_ShouldReturnCorrectCafeDto() throws Exception {
        Long cafeId = 1L;
        mockMvc.perform(get(String.format("/cafes/%s", cafeId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cafeId))
                .andExpect(jsonPath("$.name").value("First Point"));

        cafeId = 4L;
        mockMvc.perform(get(String.format("/cafes/%s", cafeId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cafeId))
                .andExpect(jsonPath("$.name").value("Blue Cup"));

        cafeId = 23L;
        mockMvc.perform(get(String.format("/cafes/%s", cafeId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cafeId))
                .andExpect(jsonPath("$.name").value("Idealist Coffee"));
    }

    @Test
    @DisplayName("Search cafes by parameters")
    void searchCafesByParameters_ValidRequest_ShouldReturnListOfCafeDtos() throws Exception {
        String cuisines = "European, Ukrainian, Fast food";
        String coworking = "false";
        String city = "Kyiv";

        MvcResult result = mockMvc.perform(get(String.format("/cafes/search?&cuisines=%s"
                                + "&coworking=%s&vegan=false&petFriendly=false&fastService=false"
                                + "&languages=English&wifi=false&businessLunch=false&freeWater="
                                + "false&boardGames=false&birthday=false&businessMeeting=false&"
                                + "childHoliday=false&romantic=false&thematicEvent=false&"
                                + "familyHoliday=false&parking=false&terrace=false&openNow=false"
                                + "&city=%s",
                        cuisines, coworking, city)))
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        List<CafeResponseDto> actual =
                List.of(objectMapper.readValue(contentAsString, CafeResponseDto[].class));
        Assertions.assertEquals(1, actual.size());
        Assertions.assertEquals("Octo", actual.get(0).getName());

        cuisines = "Fast food, Healthy";
        result = mockMvc.perform(get(String.format("/cafes/search?&cuisines=%s"
                                + "&coworking=%s&vegan=false&petFriendly=false&fastService=false"
                                + "&languages=English&wifi=false&businessLunch=false&freeWater="
                                + "false&boardGames=false&birthday=false&businessMeeting=false&"
                                + "childHoliday=false&romantic=false&thematicEvent=false&"
                                + "familyHoliday=false&parking=false&terrace=false&openNow=false"
                                + "&city=%s",
                        cuisines, coworking, city)))
                .andExpect(status().isOk())
                .andReturn();
        contentAsString = result.getResponse().getContentAsString();
        actual = List.of(objectMapper.readValue(contentAsString, CafeResponseDto[].class));
        Assertions.assertEquals(4, actual.size());
        for (CafeResponseDto cafe : actual) {
            Assertions.assertNotNull(cafe);
            assertThat(cafe.getName()).isIn("Octo", "Ta kava");
        }

        coworking = "true";
        result = mockMvc.perform(get(String.format("/cafes/search?"
                                + "&coworking=%s&vegan=false&petFriendly=false&fastService=false"
                                + "&languages=English&wifi=false&businessLunch=false&freeWater="
                                + "false&boardGames=false&birthday=false&businessMeeting=false&"
                                + "childHoliday=false&romantic=false&thematicEvent=false&"
                                + "familyHoliday=false&parking=false&terrace=false&openNow=false"
                                + "&city=%s",
                        coworking, city)))
                .andExpect(status().isOk())
                .andReturn();
        contentAsString = result.getResponse().getContentAsString();
        actual = List.of(objectMapper.readValue(contentAsString, CafeResponseDto[].class));
        Assertions.assertEquals(12, actual.size());

        city = "Lviv";
        result = mockMvc.perform(get(String.format("/cafes/search?"
                                + "&coworking=%s&vegan=false&petFriendly=false&fastService=false&"
                                + "languages=English&wifi=false&businessLunch=false&freeWater="
                                + "false&boardGames=false&birthday=false&businessMeeting=false&"
                                + "childHoliday=false&romantic=false&thematicEvent=false&"
                                + "familyHoliday=false&parking=false&terrace=false&openNow=false"
                                + "&city=%s",
                        coworking, city)))
                .andExpect(status().isOk())
                .andReturn();
        contentAsString = result.getResponse().getContentAsString();
        actual = List.of(objectMapper.readValue(contentAsString, CafeResponseDto[].class));
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    @DisplayName("Update cafe's info")
    @WithMockUser(username = "Admin", authorities = "ADMIN")
    void updateCafeInfo_ValidRequest_ShouldUpdateCafe() throws Exception {
        CreateCafeDto createCafeDto = new CreateCafeDto();
        createCafeDto.setName("New Best Cafe");
        createCafeDto.setAddress("st. best cafe 25");
        createCafeDto.setCity("Dnipro");
        createCafeDto.setPhoneNumber("(111) 111-5555");

        Long cafeId = 23L;
        Cafe cafe = cafeRepository.findById(cafeId).get();

        String jasonObject = objectMapper.writeValueAsString(createCafeDto);
        mockMvc.perform(put("/cafes/" + cafeId)
                        .content(jasonObject)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(createCafeDto.getName()))
                .andExpect(jsonPath("$.address").value(createCafeDto.getAddress()))
                .andExpect(jsonPath("$.phoneNumber").value(createCafeDto.getPhoneNumber()));

        createCafeDto.setName(cafe.getName());
        createCafeDto.setAddress(cafe.getAddress());
        createCafeDto.setCity(cafe.getCity());
        createCafeDto.setPhoneNumber(cafe.getPhoneNumber());
        jasonObject = objectMapper.writeValueAsString(createCafeDto);
        mockMvc.perform(put("/cafes/" + cafeId)
                        .content(jasonObject)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(createCafeDto.getName()))
                .andExpect(jsonPath("$.address").value(createCafeDto.getAddress()))
                .andExpect(jsonPath("$.phoneNumber").value(createCafeDto.getPhoneNumber()));
    }

    @Test
    @DisplayName("Set score")
    @WithUserDetails("second@user.com")
    @Sql(scripts = "classpath:database/users/add-four-users-to-db.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/users/clear-users-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void setScore_ValidRequest_ShouldSetScore() throws Exception {
        Long cafeId = 3L;
        int score = 2;

        mockMvc.perform(post(String.format("/cafes/scores?cafeId=%s&score=%s", cafeId, score)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cafeId))
                .andExpect(jsonPath("$.score").value(score));
    }

    @Test
    @DisplayName("Add a new cafe to DB")
    @WithMockUser(username = "Admin", authorities = "ADMIN")
    void addCafe_ValidRequest_ShouldAddCafe() throws Exception {
        CreateCafeDto createCafeDto = new CreateCafeDto();
        createCafeDto.setName("Best cafe");
        createCafeDto.setAddress("st. best cafe 21");
        createCafeDto.setCity("Dnipro");
        createCafeDto.setPhoneNumber("(111) 111-1111");

        String jasonObject = objectMapper.writeValueAsString(createCafeDto);
        Long cafeId = 26L;
        mockMvc.perform(post("/cafes")
                        .content(jasonObject)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(cafeId))
                .andExpect(jsonPath("$.name").value(createCafeDto.getName()))
                .andExpect(jsonPath("$.address").value(createCafeDto.getAddress()))
                .andExpect(jsonPath("$.phoneNumber").value(createCafeDto.getPhoneNumber()));
        cafeRepository.deleteById(cafeId);
    }

    @Test
    @DisplayName("Delete cafe")
    @WithMockUser(username = "Admin", authorities = "ADMIN")
    void deleteCafe_ValidRequest_ShouldDeleteCafe() throws Exception {
        Long cafeId = 22L;
        Cafe cafe = cafeRepository.findById(cafeId).get();
        mockMvc.perform(delete(String.format("/cafes/%s", cafeId)))
                .andExpect(status().isNoContent());

        List<Cafe> cafesLeft = cafeRepository.findAll();
        Assertions.assertEquals(24, cafesLeft.size());

        cafeRepository.save(cafe);
    }
}
