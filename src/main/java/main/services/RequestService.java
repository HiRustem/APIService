package main.services;

import main.models.Request;
import main.models.Tag;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestService {

    private final MongoTemplate mongoTemplate;

    public RequestService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void addTagToRequest(String requestId, Tag tag) {
        Query query = new Query(Criteria.where("id").is(requestId));

        Update update = new Update().addToSet("tags", tag);

        mongoTemplate.findAndModify(query, update, Request.class);
    }

    public List<Request> getRequestByTag(String tagId) {
        Query query = new Query();
        query.fields().elemMatch("tags", Criteria.where("id").is(tagId));

        return mongoTemplate.find(query, Request.class);
    }

}
