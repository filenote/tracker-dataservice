package org.example.tracker.controller;

import org.example.tracker.datamodel.Suggestion;
import org.example.tracker.service.SuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
