package main.services;

import main.models.Folder;
import main.models.Request;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class FolderService {

    private final MongoTemplate mongoTemplate;

    public FolderService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void addRequestToFolder(String folderId, Request request) {
        Query query = new Query(Criteria.where("id").is(folderId));

        Update update = new Update().addToSet("requests", request);

        mongoTemplate.findAndModify(query, update, Folder.class);
    }

    public Set<Request> getRequestByFolder(String folderId) {
        Query query = new Query(Criteria.where("id").is(folderId));
        List<Folder> folder = mongoTemplate.find(query, Folder.class);

        return folder.stream().findFirst().get().getRequests();
    }
}
