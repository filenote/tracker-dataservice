package org.example.tracker.controller;

import org.example.tracker.datamodel.Suggestion;
import org.example.tracker.service.SuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suggestion")
public class SuggestionController {

    @Autowired
    private SuggestionService service;

    @GetMapping
    public List<Suggestion> getAllSuggestions() {
        return service.getAllSuggestions();
    }

    @PostMapping
    public Suggestion insertSuggestion(@RequestBody Suggestion suggestion) {
        return service.insertSuggestion(suggestion);
    }
}
