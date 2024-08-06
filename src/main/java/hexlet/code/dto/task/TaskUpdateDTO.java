package hexlet.code.dto.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.Set;

@Getter
@Setter
public class TaskUpdateDTO {
    private JsonNullable<Integer> index;
    @NotBlank
    private JsonNullable<String> title; //name

    private JsonNullable<String> content; //description

    @JsonProperty("assignee_id")
    private JsonNullable<Long> assigneeId;

    @NotNull
    private JsonNullable<String> status;

    private JsonNullable<Set<Long>> taskLabelIds;

}
