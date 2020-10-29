package org.example.tracker.repository;

import org.example.tracker.datamodel.Comment;
import org.example.tracker.datamodel.Suggestion;
import org.example.tracker.datamodel.UpdateCommentRequest;
import org.example.tracker.datamodel.request.UpdateCurrentStageRequest;
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

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
public class SuggestionRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Value("${service.collection.suggestion}")
    private String collection;

    public List<Suggestion> getAllSuggestions() {
        MatchOperation match = Aggregation.match(where("_id").exists(true));
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
        Query query = query(where("_id").is(id));
        return mongoTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), Suggestion.class);
    }

    public Suggestion getSuggestionById(UUID id) {
        MatchOperation match = Aggregation.match(where("_id").is(id));
        LimitOperation limit = Aggregation.limit(1);
        Aggregation aggregation = Aggregation.newAggregation(match, limit);
        return mongoTemplate.aggregate(aggregation, collection, Suggestion.class).getMappedResults().iterator().next();
    }

    public Suggestion updateCurrentStage(UpdateCurrentStageRequest request) {
        Update update = new Update();
        update.set("currentStage", request.getStage().getStage());
        Query query = query(where("_id").is(request.getId()));
        return mongoTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), Suggestion.class, collection);
    }

    public Comment insertComment(UUID suggestionId, Comment comment) {
        Update update = new Update().addToSet("comments", comment);
        Query query = query(where("_id").is(suggestionId));
        long updateResult = mongoTemplate.updateFirst(query, update, Suggestion.class, collection).getModifiedCount();
        if (updateResult != 0) return comment;
        return null;
    }

    public Suggestion updateComment(UUID suggestionId, UUID commentId, UpdateCommentRequest comment, String username) {
        Update update = new Update()
                .set("comments.$.text", comment.getText())
                .set("comments.$.updatedDate", Instant.now());
        Query query = query(where("_id").is(suggestionId).and("comments").elemMatch(where("_id").is(commentId).and("username").is(username)));
        return mongoTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), Suggestion.class, collection);
    }
}
