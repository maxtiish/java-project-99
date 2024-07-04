package hexlet.code.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class TaskStatusUpdateDTO {
    @NotNull
    private JsonNullable<Long> id;

    @NotNull
    private JsonNullable<String> name;

    @NotNull
    private JsonNullable<String> slug;
}
