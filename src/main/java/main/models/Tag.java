package main.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@Data
@Document
@Schema(description = "Сущность тега")
public class Tag {
    @Id
    @Schema(description = "Id тега")
    private String id;
    @Schema(description = "Имя тега")
    private String name;

    public Tag(String name) {
        this.name = name;
    }
}
