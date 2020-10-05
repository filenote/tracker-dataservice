package org.example.tracker.repository;

import org.example.tracker.datamodel.Suggestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class SuggestionRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Value("${service.collection.suggestion}")
    private String collection;

    public List<Suggestion> getAllSuggestions() {
        MatchOperation match = Aggregation.match(Criteria.where("_id").exists(true));
        LimitOperation limit = Aggregation.limit(500); // limiting for now
        Aggregation aggregation = Aggregation.newAggregation(match, limit);
        return mongoTemplate.aggregate(aggregation, collection, Suggestion.class).getMappedResults();
    }

    public Suggestion insertSuggestion(Suggestion suggestion) {
        return mongoTemplate.insert(suggestion, collection);
    }

    public Suggestion upvoteSuggestion(UUID id) {
        Update update = new Update();
        update.inc("vote.amount");
        Query query = Query.query(Criteria.where("_id").is(id));
        return mongoTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), Suggestion.class);
    }

    public Suggestion getSuggestionById(UUID id) {
        MatchOperation match = Aggregation.match(Criteria.where("_id").is(id));
        LimitOperation limit = Aggregation.limit(1);
        Aggregation aggregation = Aggregation.newAggregation(match, limit);
        return mongoTemplate.aggregate(aggregation, collection, Suggestion.class).getMappedResults().iterator().next();
    }
}
