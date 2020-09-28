package org.example.tracker.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import org.example.tracker.datamodel.Suggestion;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class SuggestionRepository {

    private static List<Suggestion> suggestions;
    static {
        try {
            URL resource = Resources.getResource("test_data.json");
            String s = Resources.toString(resource, Charset.defaultCharset());
            Suggestion[] suggestionArray = new ObjectMapper().readValue(s, Suggestion[].class);
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
}
