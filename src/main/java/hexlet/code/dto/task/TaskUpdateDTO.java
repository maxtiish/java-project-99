package hexlet.code.dto.task;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.Set;

@Getter
@Setter
public class TaskUpdateDTO {
    private JsonNullable<String> name;

    private JsonNullable<String> description;

    private JsonNullable<Long> assigneeId;

    private JsonNullable<String> status;

    private JsonNullable<Set<Long>> taskLabelIds;

}
