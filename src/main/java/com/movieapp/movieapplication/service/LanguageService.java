package com.movieapp.movieapplication.service;

import com.movieapp.movieapplication.model.Language;
import com.movieapp.movieapplication.repository.LanguageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LanguageService {

    private final LanguageRepository languageRepository;

    public LanguageService(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    public List<Language> getAllLanguages() {
        return languageRepository.findAll();
    }

    public Optional<Language> getLanguageById(String id) {
        return languageRepository.findById(id);
    }

    public Language addLanguage(Language language) {
        return languageRepository.save(language);
    }

    public void deleteLanguage(String id) {
        languageRepository.deleteById(id);
    }

    public Language updateLanguage(String id, Language language) {
        return languageRepository.findById(id).map(existingLanguage -> {
            existingLanguage.setName(language.getName());
            existingLanguage.setCode(language.getCode());
            return languageRepository.save(existingLanguage);
        }).orElseThrow(() -> new RuntimeException("Language not found with id: " + id));
    }
}
