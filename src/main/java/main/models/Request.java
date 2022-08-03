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
@Schema(description = "Сущность запроса")
public class Request {
    @Id
    @Schema(description = "Id запроса")
    private String id;
    @Schema(description = "Текст запроса")
    private String text;
    @Schema(description = "Дата изменения")
    private Long modifiedDate;
    @Schema(description = "Длина запроса")
    private Long length;

    @Schema(description = "Список тегов запроса")
    private Set<Tag> tags;

    public Request(String text, Long modifiedDate, Long length) {
        this.text = text;
        this.modifiedDate = modifiedDate;
        this.length = length;
    }


}
