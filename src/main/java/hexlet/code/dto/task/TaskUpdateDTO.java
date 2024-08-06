package hexlet.code.dto.task;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.Set;

@Getter
@Setter
public class TaskUpdateDTO {
    @NotNull
    private JsonNullable<String> name;

    @NotNull
    private JsonNullable<String> description;

    @NotNull
    private JsonNullable<Long> assigneeId;

    @NotNull
    private JsonNullable<String> status;

    @NotNull
    private JsonNullable<Set<Long>> taskLabelIds;

}
