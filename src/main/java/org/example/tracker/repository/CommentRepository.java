package org.example.tracker.repository;

import org.example.tracker.datamodel.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class CommentRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Value("${service.collection.comment}")
    private String collection;

    public List<Comment> getAllCommentsForSuggestion(UUID suggestionId) {
        MatchOperation match = Aggregation.match(Criteria.where("suggestionId").is(suggestionId));
        Aggregation aggregation = Aggregation.newAggregation(match);
        return new ArrayList<>(mongoTemplate.aggregate(aggregation, collection, Comment.class).getMappedResults());
    }

    public Comment insert(Comment comment) {
        return mongoTemplate.insert(comment, collection);
    }
}
