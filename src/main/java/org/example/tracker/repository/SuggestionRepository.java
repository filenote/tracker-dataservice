package org.example.tracker.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.io.Resources;
import org.example.tracker.datamodel.Suggestion;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

@Repository
public class SuggestionRepository {

    private static List<Suggestion> suggestions;
    static {
        try {
            ObjectMapper o = new ObjectMapper().registerModule(new Jdk8Module()).registerModule(new JavaTimeModule());
            URL resource = Resources.getResource("test_data.json");
            String s = Resources.toString(resource, Charset.defaultCharset());
            Suggestion[] suggestionArray = o.readValue(s, Suggestion[].class);
            suggestions = new ArrayList<>(Arrays.asList(suggestionArray));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Suggestion> getAllSuggestions() {
        return suggestions;
    }

    public Suggestion insertSuggestion(Suggestion suggestion) {
        boolean add = suggestions.add(suggestion);
        return add ? suggestion : null;
    }

    public Suggestion upvoteSuggestion(UUID id) {
        suggestions.forEach(suggestion -> {
            if (suggestion.getId().equals(id)) {
                Long current = suggestion.getVote().getAmount();
                suggestion.getVote().setAmount(current + 1);
            }
        });
        return suggestions.stream().filter(suggestion -> suggestion.getId().equals(id)).findFirst().orElse(null);
    }
}
