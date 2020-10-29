package org.example.tracker.controller;

import org.example.tracker.datamodel.Comment;
import org.example.tracker.datamodel.CommentRequest;
import org.example.tracker.datamodel.Suggestion;
import org.example.tracker.datamodel.UpdateCommentRequest;
import org.example.tracker.service.SuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/suggestion/{id}/comment")
public class CommentController {

    @Autowired
    private SuggestionService service;

    @PostMapping
    public Comment insertComment(
            @PathVariable(name = "id") UUID suggestionId,
            @RequestBody CommentRequest commentRequest,
            Principal principal) {
        return service.insertComment(suggestionId, commentRequest, principal.getName());
    }

    @PutMapping("/{commentId}")
    public Suggestion updateComment(
            @PathVariable(name = "id") UUID suggestionId,
            @PathVariable(name = "commentId") UUID commentId,
            @RequestBody UpdateCommentRequest comment,
            Principal principal) {
        return service.updateComment(suggestionId, commentId, comment, principal.getName());
    }
}
