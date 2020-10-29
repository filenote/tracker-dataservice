package org.example.tracker.controller;

import org.example.tracker.datamodel.Suggestion;
import org.example.tracker.datamodel.request.UpdateCurrentStageRequest;
import org.example.tracker.service.SuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.Collections.sort;

@RestController
@RequestMapping("/api/suggestion")
public class SuggestionController {

    @Autowired
    private SuggestionService service;

    @GetMapping
    public List<Suggestion> getAllSuggestions() {
        List<Suggestion> suggestions = new ArrayList<>(service.getAllSuggestions());
        sort(suggestions);
        return suggestions;
    }

    @GetMapping("/{id}")
    public Suggestion getSuggestionById(@PathVariable UUID id) {
        return service.getSuggestionById(id);
    }

    @PostMapping
    public Suggestion insertSuggestion(@RequestBody Suggestion suggestion) {
        return service.insertSuggestion(suggestion);
    }

    @PostMapping("/{id}/upvote")
    public Suggestion upvoteSuggestion(@PathVariable UUID id) {
        return service.upvoteSuggestion(id);
    }

    @PostMapping("/stage")
    public Suggestion updateCurrentStage(@RequestBody UpdateCurrentStageRequest request) {
        return service.updateCurrentStage(request);
    }
}
