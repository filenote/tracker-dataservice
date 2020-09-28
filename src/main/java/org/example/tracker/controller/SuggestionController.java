package org.example.tracker.controller;

import org.example.tracker.datamodel.Suggestion;
import org.example.tracker.service.SuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

    @PostMapping("/{id}/upvote")
    public Suggestion upvoteSuggestion(@PathVariable UUID id) {
        return service.upvoteSuggestion(id);
    }
}
