package org.example.tracker.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.io.Resources;
import org.example.tracker.datamodel.Comment;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class CommentRepository {

    private static List<Comment> comments;
    static {
        try {
            ObjectMapper o = new ObjectMapper().registerModule(new Jdk8Module()).registerModule(new JavaTimeModule());
            URL resource = Resources.getResource("comments_test_data.json");
            String s = Resources.toString(resource, Charset.defaultCharset());
            Comment[] array = o.readValue(s, Comment[].class);
            comments = new ArrayList<>(Arrays.asList(array));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Comment> getAllCommentsForSuggestion(UUID suggestionId) {
        return comments.stream()
                .filter(comment -> comment.getSuggestionId().equals(suggestionId))
                .collect(Collectors.toList());
    }

    public Comment insert(Comment comment) {
        boolean add = comments.add(comment);
        return add ? comment : null;
    }
}
