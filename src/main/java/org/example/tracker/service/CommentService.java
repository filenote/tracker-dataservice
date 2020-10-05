package org.example.tracker.service;

import org.example.tracker.datamodel.Comment;
import org.example.tracker.datamodel.CommentRequest;
import org.example.tracker.exception.ElementNotFound;
import org.example.tracker.exception.FailedToInsert;
import org.example.tracker.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class CommentService {

    @Autowired
    private CommentRepository repository;

    public List<Comment> getAllCommentsForSuggestion(UUID suggestionId) {
        List<Comment> comments = repository.getAllCommentsForSuggestion(suggestionId);
        if (CollectionUtils.isEmpty(comments)) {
            throw new ElementNotFound("No data was found for " + suggestionId);
        }
        Comparator<Comment> sortFunction = Collections.reverseOrder(Comparator.comparing(Comment::getCreatedDate));
        comments.sort(sortFunction);
        return comments;
    }

    public Comment addCommentToSuggestion(UUID suggestionId, CommentRequest request, String username) {
        Comment comment = new Comment();
        comment.setId(UUID.randomUUID());
        comment.setComment(request.getComment());
        comment.setSuggestionId(suggestionId);
        ZonedDateTime now = ZonedDateTime.now();
        comment.setUsername(username);
        comment.setCreatedDate(now);
        comment.setUpdatedDate(now);
        Comment inserted = repository.insert(comment);
        if (inserted == null) {
            throw new FailedToInsert("Failed to inserted comment.");
        }
        return inserted;
    }
}
