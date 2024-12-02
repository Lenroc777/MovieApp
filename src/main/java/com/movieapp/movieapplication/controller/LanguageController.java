package com.movieapp.movieapplication.controller;

import com.movieapp.movieapplication.model.Language;
import com.movieapp.movieapplication.service.LanguageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/languages")
public class LanguageController {

    private final LanguageService languageService;

    public LanguageController(LanguageService languageService) {
        this.languageService = languageService;
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<Language>> getAllLanguages() {
        return ResponseEntity.ok(languageService.getAllLanguages());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Language> getLanguageById(@PathVariable String id) {
        return languageService.getLanguageById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Language> addLanguage(@RequestBody Language language) {
        return ResponseEntity.ok(languageService.addLanguage(language));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Language> updateLanguage(@PathVariable String id, @RequestBody Language language) {
        return ResponseEntity.ok(languageService.updateLanguage(id, language));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLanguage(@PathVariable String id) {
        languageService.deleteLanguage(id);
        return ResponseEntity.noContent().build();
    }
}
