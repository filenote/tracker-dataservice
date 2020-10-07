package org.example.tracker.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.example.tracker.datamodel.Role;
import org.example.tracker.datamodel.SimplerGrantedAuthority;
import org.example.tracker.datamodel.UserCredentials;
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
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserCredentialsRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${service.collection.user}")
    private String collection;

    public UserCredentials findUserCredentialsByUsername(String username) {
        MatchOperation match = Aggregation.match(Criteria.where("username").is(username));
        LimitOperation limit = Aggregation.limit(1);
        Aggregation aggregation = Aggregation.newAggregation(match, limit);
        List<UserCredentials> results = mongoTemplate.aggregate(aggregation, collection, UserCredentials.class).getMappedResults();
        if (CollectionUtils.isEmpty(results)) return null;
        return results.iterator().next();
    }

    public boolean doesUsernameExist(String username) {
        MatchOperation match = Aggregation.match(Criteria.where("username").is(username));
        LimitOperation limit = Aggregation.limit(1);
        Aggregation aggregation = Aggregation.newAggregation(match, limit);
        List<UserCredentials> results = mongoTemplate.aggregate(aggregation, collection, UserCredentials.class).getMappedResults();
        return CollectionUtils.isNotEmpty(results);
    }

    public boolean addAccount(UserCredentials credentials) {
        mongoTemplate.insert(UserCredentials.class).inCollection(collection).one(credentials);
        return true;
    }

    public UserCredentials addAuthority(String username, Role role, boolean initialize) {
        Query query = Query.query(Criteria.where("username").is(username));
        UpdateDefinition update;
        if (initialize) {
            update = new Update().set("authorities", List.of(SimplerGrantedAuthority.of(role)));
        } else {
            update = new Update().addToSet("authorities", SimplerGrantedAuthority.of(role));
        }
        return mongoTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), UserCredentials.class, collection);
    }
}
