package com.example.cafehub.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.cafehub.dto.language.LanguageCreateRequestDto;
import com.example.cafehub.dto.language.LanguageResponseDto;
import com.example.cafehub.model.Language;
import com.example.cafehub.repository.LanguageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LanguageControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private LanguageRepository languageRepository;

    @BeforeAll
    static void setUp(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("Get all languages")
    void getAllLanguages_ValidRequest_ShouldReturnListOfDtos() throws Exception {
        MvcResult result = mockMvc.perform(get("/languages"))
                        .andExpect(status().isOk())
                        .andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        List<LanguageResponseDto> actual =
                List.of(objectMapper.readValue(contentAsString, LanguageResponseDto[].class));

        Assertions.assertEquals(10, actual.size());

        List<String> languageNames = actual.stream()
                .map(LanguageResponseDto::getName)
                .toList();

        Assertions.assertTrue(languageNames.contains("English"));
        Assertions.assertTrue(languageNames.contains("Italian"));
        Assertions.assertTrue(languageNames.contains("Ukrainian"));
    }

    @Test
    @DisplayName("Get language by id")
    void getLanguageById_ValidRequest_ShouldReturnLanguageDto() throws Exception {
        Long languageId = 3L;
        mockMvc.perform(get(String.format("/languages/%s", languageId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(languageId))
                .andExpect(jsonPath("$.name").value("Polish"));

        languageId = 5L;
        mockMvc.perform(get(String.format("/languages/%s", languageId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(languageId))
                .andExpect(jsonPath("$.name").value("French"));
    }

    @Test
    @DisplayName("Get language by wrong id. Not found response status")
    void getLanguageById_WrongId_NotFoundResponseStatus() throws Exception {
        Long languageId = 15L;
        mockMvc.perform(get(String.format("/languages/%s", languageId)))
                .andExpect(status().isNotFound());

        languageId = 21L;
        mockMvc.perform(get(String.format("/languages/%s", languageId)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Add new language")
    @WithMockUser(username = "Admin", authorities = "ADMIN")
    void addLanguage_ValidRequest_ShouldAddLanguage() throws Exception {
        LanguageCreateRequestDto languageCreateDto = new LanguageCreateRequestDto();
        languageCreateDto.setName("Esperanto");

        String jasonObject = objectMapper.writeValueAsString(languageCreateDto);
        mockMvc.perform(post("/languages")
                        .content(jasonObject)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(languageCreateDto.getName()));

        languageCreateDto.setName("Irish");
        jasonObject = objectMapper.writeValueAsString(languageCreateDto);
        mockMvc.perform(post("/languages")
                        .content(jasonObject)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(languageCreateDto.getName()));

        languageRepository.deleteById(11L);
        languageRepository.deleteById(12L);
    }

    @Test
    @DisplayName("Update language")
    @WithMockUser(username = "Admin", authorities = "ADMIN")
    void updateLanguage_ValidRequest_ShouldUpdateLanguage() throws Exception {
        Long languageId = 5L;
        String languageName = languageRepository.findById(languageId).get().getName();
        LanguageCreateRequestDto languageCreateDto = new LanguageCreateRequestDto();
        languageCreateDto.setName("Greek");
        String jasonObject = objectMapper.writeValueAsString(languageCreateDto);
        mockMvc.perform(put(String.format("/languages/%s", languageId))
                        .content(jasonObject)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(languageId))
                .andExpect(jsonPath("$.name").value(languageCreateDto.getName()));

        languageCreateDto.setName(languageName);
        jasonObject = objectMapper.writeValueAsString(languageCreateDto);
        mockMvc.perform(put(String.format("/languages/%s", languageId))
                        .content(jasonObject)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(languageId))
                .andExpect(jsonPath("$.name").value(languageCreateDto.getName()));
    }

    @Test
    @DisplayName("Delete language by id")
    @WithMockUser(username = "Admin", authorities = "ADMIN")
    void deleteLanguage_ValidId_ShouldDeleteLanguage() throws Exception {
        Long languageId = 10L;
        Language language = languageRepository.findById(languageId).get();
        mockMvc.perform(delete(String.format("/languages/%s", languageId)))
                .andExpect(status().isNoContent());

        Assertions.assertEquals(9, languageRepository.findAll().size());

        languageRepository.save(language);
    }
}
