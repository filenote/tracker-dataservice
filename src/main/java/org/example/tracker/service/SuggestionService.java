package org.example.tracker.service;

import com.google.common.collect.ImmutableList;
import org.example.tracker.datamodel.Stage;
import org.example.tracker.datamodel.Suggestion;
import org.example.tracker.datamodel.Vote;
import org.example.tracker.repository.SuggestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class SuggestionService {

    @Autowired
    private SuggestionRepository repository;

    private static final List<Stage> defaultStages = ImmutableList.of(
            new Stage(true, "Submitted"),
            new Stage(false, "Accepted"),
            new Stage(false, "Dev Started"),
            new Stage(false, "Dev Complete"),
            new Stage(false, "Released")
    );

    private static final Vote defaultVote = new Vote(false, 0L);

    public List<Suggestion> getAllSuggestions() {
        return repository.getAllSuggestions();
    }

    public Suggestion insertSuggestion(Suggestion suggestion) {
        if (suggestion.getId() == null) {
            suggestion.setId(UUID.randomUUID());
        }

        if (CollectionUtils.isEmpty(suggestion.getStages())) {
            suggestion.setStages(List.copyOf(defaultStages));
        }

        if (suggestion.getVote() == null) {
            suggestion.setVote(new Vote(defaultVote));
        }

        return repository.insertSuggestion(suggestion);
    }

    public Suggestion upvoteSuggestion(UUID id) {
        return repository.upvoteSuggestion(id);
    }
}
