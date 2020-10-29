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
        suggestions.forEach(suggestion -> {
            sortStages(suggestion);
            sortComments(suggestion);
        });

        // this is here because we now need to fill currentStage field
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
        sortComments(suggestion);
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
        sortComments(suggestion);
        return suggestion;
    }

    private void sortStages(Suggestion suggestion) {
        if (CollectionUtils.isNotEmpty(suggestion.getStages())) {
            suggestion.getStages().sort(comparing(Stage::getStage));
        }
    }

    private void sortComments(Suggestion suggestion) {
        if (CollectionUtils.isNotEmpty(suggestion.getComments())) {
            suggestion.getComments().sort(comparing(Comment::getCreatedDate).reversed());
        }
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

    public Suggestion updateComment(UUID suggestionId, UUID commentId, UpdateCommentRequest comment, String username) {
        Suggestion suggestion = repository.updateComment(suggestionId, commentId, comment, username);
        if (suggestion == null) {
            throw new ElementNotFound("No date found to update.");
        }
        sortStages(suggestion);
        sortComments(suggestion);
        enableStagesUntilCurrentStage(suggestion);
        return suggestion;
    }
}
