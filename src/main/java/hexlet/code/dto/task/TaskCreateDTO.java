package hexlet.code.dto.task;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class TaskCreateDTO {
    private Long assigneeId;

    @NotNull
    private Integer index;

    @NotNull
    private String status;

    private String name;

    private String description;

    @NotNull
    private Set<Long> taskLabelIds;
}
