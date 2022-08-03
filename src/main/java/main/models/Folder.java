package main.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@NoArgsConstructor
@Data
@Document
@Schema(description = "Сущность папки")
public class Folder {
    @Id
    @Schema(description = "Id папки")
    private String id;

    @Schema(description = "Имя папки")
    private String folderName;

    @Schema(description = "Список запросов в папке")
    private Set<Request> requests;

    public Folder(String folderName) {
        this.folderName = folderName;
    }
}