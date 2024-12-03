package com.movieapp.movieapplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movieapp.movieapplication.model.Language;
import com.movieapp.movieapplication.service.LanguageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class LanguageControllerTest {

    @Mock
    private LanguageService languageService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        LanguageController languageController = new LanguageController(languageService);
        mockMvc = MockMvcBuilders.standaloneSetup(languageController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetAllLanguages() throws Exception {
        Language language1 = new Language("1", "English", "EN");
        Language language2 = new Language("2", "Spanish", "ES");

        when(languageService.getAllLanguages()).thenReturn(List.of(language1, language2));

        mockMvc.perform(get("/api/languages/get-all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("English"))
                .andExpect(jsonPath("$[1].name").value("Spanish"));

        verify(languageService, times(1)).getAllLanguages();
    }

    @Test
    void testGetLanguageById_Found() throws Exception {
        String languageId = "1";
        Language language = new Language(languageId, "English", "EN");

        when(languageService.getLanguageById(languageId)).thenReturn(Optional.of(language));

        mockMvc.perform(get("/api/languages/" + languageId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("English"));

        verify(languageService, times(1)).getLanguageById(languageId);
    }

    @Test
    void testGetLanguageById_NotFound() throws Exception {
        String languageId = "1";

        when(languageService.getLanguageById(languageId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/languages/" + languageId))
                .andExpect(status().isNotFound());

        verify(languageService, times(1)).getLanguageById(languageId);
    }

    @Test
    void testAddLanguage() throws Exception {
        Language language = new Language("1", "English", "EN");

        when(languageService.addLanguage(any(Language.class))).thenReturn(language);

        mockMvc.perform(post("/api/languages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(language)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("English"));

        verify(languageService, times(1)).addLanguage(any(Language.class));
    }

    @Test
    void testUpdateLanguage_Found() throws Exception {
        String languageId = "1";
        Language updatedLanguage = new Language(languageId, "Spanish", "ES");

        when(languageService.updateLanguage(eq(languageId), any(Language.class))).thenReturn(updatedLanguage);

        mockMvc.perform(put("/api/languages/" + languageId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedLanguage)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Spanish"));

        verify(languageService, times(1)).updateLanguage(eq(languageId), any(Language.class));
    }


    @Test
    void testDeleteLanguage() throws Exception {
        String languageId = "1";

        doNothing().when(languageService).deleteLanguage(languageId);

        mockMvc.perform(delete("/api/languages/" + languageId))
                .andExpect(status().isNoContent());

        verify(languageService, times(1)).deleteLanguage(languageId);
    }
}