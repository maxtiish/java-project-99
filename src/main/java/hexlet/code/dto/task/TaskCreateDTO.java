package hexlet.code.dto.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import hexlet.code.model.TaskStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskCreateDTO {
    @JsonProperty("assignee_id")
    @NotNull
    private Long assigneeId;

    private Integer index;

    @NotNull
    private TaskStatus status;

    @Size(min = 1)
    @NotNull
    private String name;

    private String description;
}
