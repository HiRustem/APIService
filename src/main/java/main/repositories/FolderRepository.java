package main.repositories;

import main.models.Folder;
import main.models.Request;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FolderRepository extends MongoRepository<Folder, String> {
    Folder findByFolderName(String folderName);
}
