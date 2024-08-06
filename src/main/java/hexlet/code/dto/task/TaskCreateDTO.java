package hexlet.code.dto.task;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class TaskCreateDTO {
    @NotNull
    private Long assigneeId;

    @NotNull
    private Integer index;

    @NotNull
    private String status;

    @Size(min = 1)
    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private Set<Long> taskLabelIds;
}
