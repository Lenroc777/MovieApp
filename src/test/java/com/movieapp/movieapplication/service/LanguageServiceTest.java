package com.movieapp.movieapplication.service;

import com.movieapp.movieapplication.model.Language;
import com.movieapp.movieapplication.repository.LanguageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class LanguageServiceTest {

    @Mock
    private LanguageRepository languageRepository;

    private LanguageService languageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        languageService = new LanguageService(languageRepository);
    }

    @Test
    void testGetAllLanguages() {
        Language language = new Language("1", "English", "EN");
        when(languageRepository.findAll()).thenReturn(List.of(language));

        List<Language> languages = languageService.getAllLanguages();

        assertEquals(1, languages.size());
        assertEquals("English", languages.get(0).getName());
        verify(languageRepository, times(1)).findAll();
    }

    @Test
    void testGetLanguageById_Found() {
        Language language = new Language("1", "English", "EN");
        when(languageRepository.findById("1")).thenReturn(Optional.of(language));

        Optional<Language> result = languageService.getLanguageById("1");

        assertEquals(true, result.isPresent());
        assertEquals("English", result.get().getName());
        verify(languageRepository, times(1)).findById("1");
    }

    @Test
    void testGetLanguageById_NotFound() {
        when(languageRepository.findById("1")).thenReturn(Optional.empty());

        Optional<Language> result = languageService.getLanguageById("1");

        assertEquals(false, result.isPresent());
        verify(languageRepository, times(1)).findById("1");
    }

    @Test
    void testAddLanguage() {
        Language language = new Language("1", "English", "EN");
        when(languageRepository.save(language)).thenReturn(language);

        Language result = languageService.addLanguage(language);

        assertEquals("English", result.getName());
        assertEquals("EN", result.getCode());
        verify(languageRepository, times(1)).save(language);
    }

    @Test
    void testDeleteLanguage() {
        String languageId = "1";

        languageService.deleteLanguage(languageId);

        verify(languageRepository, times(1)).deleteById(languageId);
    }

    @Test
    void testUpdateLanguage_Found() {
        String languageId = "1";
        Language existingLanguage = new Language(languageId, "English", "EN");
        Language updatedLanguage = new Language(languageId, "Spanish", "ES");

        when(languageRepository.findById(languageId)).thenReturn(Optional.of(existingLanguage));
        when(languageRepository.save(any(Language.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Language result = languageService.updateLanguage(languageId, updatedLanguage);

        assertEquals("Spanish", result.getName());
        assertEquals("ES", result.getCode());
        verify(languageRepository, times(1)).findById(languageId);
        verify(languageRepository, times(1)).save(existingLanguage);
    }

}