package hexlet.code.dto.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import hexlet.code.model.TaskStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TaskDTO {
    private Long id;

    private Integer index;

    @JsonProperty("assignee_id")
    private Long assigneeId;

    private String name;

    private String description;

    private TaskStatus status;

    private LocalDate createdAt;
}