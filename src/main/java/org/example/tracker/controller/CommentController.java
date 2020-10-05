package org.example.tracker.controller;

import org.example.tracker.datamodel.Comment;
import org.example.tracker.datamodel.CommentRequest;
import org.example.tracker.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    private CommentService service;

    @GetMapping("/suggestion/{id}")
    public List<Comment> getAllCommentsForSuggestion(@PathVariable UUID id) {
        return service.getAllCommentsForSuggestion(id);
    }

    @PostMapping("/suggestion/{id}")
    public Comment addCommentToSuggestion(
            @PathVariable(name = "id") UUID suggestionId,
            @RequestBody CommentRequest request,
            Principal principal) {
        return service.addCommentToSuggestion(suggestionId, request, principal.getName());
    }
}
