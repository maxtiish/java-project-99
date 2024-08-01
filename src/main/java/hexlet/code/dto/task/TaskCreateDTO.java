package hexlet.code.dto.task;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class TaskCreateDTO {
    @NotNull
    private Long assigneeId;

    private Integer index;

    @NotNull
    private String status;

    @Size(min = 1)
    @NotNull
    private String name;

    private String description;

    private LocalDate createdAt;

    private Set<Long> taskLabelIds;
}
