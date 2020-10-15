package org.example.tracker.service;

import com.google.common.collect.ImmutableList;
import org.apache.commons.collections4.CollectionUtils;
import org.example.tracker.datamodel.*;
import org.example.tracker.datamodel.request.UpdateCurrentStageRequest;
import org.example.tracker.exception.ElementNotFound;
import org.example.tracker.repository.SuggestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static org.apache.commons.lang3.BooleanUtils.isFalse;

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
        suggestions.forEach(suggestion -> {
            suggestion.getStages()
                .sort(comparing(Stage::getStage));

            suggestion.getComments()
                    .sort(comparing(Comment::getCreatedDate).reversed());
        });

        // this is here because we now need to fill currentStage field
        fixCurrentStageField(suggestions);
        enableStagesUntilCurrentStage(suggestions);

        return suggestions;
    }

    private void enableStagesUntilCurrentStage(List<Suggestion> suggestions) {
        suggestions.forEach(suggestion -> suggestion.getStages().forEach(stage -> {
            if (stage.getStage() <= suggestion.getCurrentStage()) {
                stage.setEnabled(true);
            }
        }));
    }

    private void fixCurrentStageField(List<Suggestion> suggestions) {
        List<Suggestion>  nullCurrentStage = suggestions.stream().filter(suggestion -> suggestion.getCurrentStage() == null).collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(nullCurrentStage)) {
            nullCurrentStage.stream().map(suggestion -> {
                List<Stage> stages = suggestion.getStages();
                int index = -1;
                for (int i = 0; i < stages.size(); i++) {
                    if (isFalse(stages.get(i).getEnabled())) {
                        index = i - 1;
                        break;
                    }
                }

                if (index == -1) {
                    index = stages.size() - 1;
                }

                return new UpdateCurrentStageRequest(suggestion.getId(), stages.get(index));
            }).forEach(this::updateCurrentStage);
        }
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
        suggestion.setComments(Collections.emptyList());
        Instant now = Instant.now();
        suggestion.setCreatedDate(now);
        suggestion.setLastUpdatedDate(now);
        suggestion.setCurrentStage(0);

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
        sortStages(suggestion);
        enableStagesUntilCurrentStage(suggestion);
        return suggestion;
    }

    private void enableStagesUntilCurrentStage(Suggestion suggestion) {
        suggestion.getStages().forEach(stage -> {
            if (stage.getStage() <= suggestion.getCurrentStage()) {
                stage.setEnabled(true);
            }
        });
    }


    public Suggestion updateCurrentStage(UpdateCurrentStageRequest request) {
        Suggestion suggestion = repository.updateCurrentStage(request);
        sortStages(suggestion);
        enableStagesUntilCurrentStage(suggestion);
        return suggestion;
    }

    private void sortStages(Suggestion suggestion) {
        suggestion.getStages().sort(comparing(Stage::getStage));
    }

    public Comment insertComment(UUID suggestionId, CommentRequest commentRequest, String username) {
        Comment comment = new Comment();
        comment.setId(UUID.randomUUID());
        comment.setText(commentRequest.getText());
        comment.setUsername(username);
        Instant instant = Instant.now();
        comment.setCreatedDate(instant);
        comment.setUpdatedDate(instant);
        return repository.insertComment(suggestionId, comment);
    }
}
