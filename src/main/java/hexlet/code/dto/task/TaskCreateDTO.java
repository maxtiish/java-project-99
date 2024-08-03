package hexlet.code.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class TaskCreateDTO {
    @NotBlank
    private Long assigneeId;

    private Integer index;

    @NotBlank
    private String status;

    @Size(min = 1)
    @NotBlank
    private String name;

    private String description;

    private LocalDate createdAt;

    private Set<Long> taskLabelIds;
}
