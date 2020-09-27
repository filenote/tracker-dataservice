package org.example.tracker.service;

import org.example.tracker.datamodel.Suggestion;
import org.example.tracker.repository.SuggestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SuggestionService {

    @Autowired
    private SuggestionRepository repository;

    public List<Suggestion> getAllSuggestions() {
        return repository.getAllSuggestions();
    }
}
