package org.example.tracker.service;

import com.google.common.collect.ImmutableList;
import org.example.tracker.datamodel.Stage;
import org.example.tracker.datamodel.Suggestion;
import org.example.tracker.datamodel.Vote;
import org.example.tracker.exception.ElementNotFound;
import org.example.tracker.repository.SuggestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.ZonedDateTime;
import java.util.*;

import static java.util.Comparator.comparing;

@Service
public class SuggestionService {

    @Autowired
    private SuggestionRepository repository;

    private static final List<Stage> defaultStages = ImmutableList.of(
            new Stage(0, true, "Submitted"),
            new Stage(1, false, "Accepted"),
            new Stage(2, false, "Dev Started"),
            new Stage(3, false, "Dev Complete"),
            new Stage(4, false, "Released")
    );

    private static final Vote defaultVote = new Vote(false, 0L);

    public List<Suggestion> getAllSuggestions() {
        List<Suggestion> suggestions = repository.getAllSuggestions();
        suggestions.forEach(suggestion -> suggestion
                .getStages()
                .sort(comparing(Stage::getStage)));
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

        ZonedDateTime now = ZonedDateTime.now();
        suggestion.setCreatedDate(now);
        suggestion.setLastUpdatedDate(now);

        return repository.insertSuggestion(suggestion);
    }

    public Suggestion upvoteSuggestion(UUID id) {
        return repository.upvoteSuggestion(id);
    }

    public Suggestion getSuggestionById(UUID id) {
        Suggestion suggestion = repository.getSuggestionById(id);
        if (suggestion == null) {
            throw new ElementNotFound("No data found.");
        }
        suggestion.getStages().sort(comparing(Stage::getStage));
        return repository.getSuggestionById(id);
    }


}
