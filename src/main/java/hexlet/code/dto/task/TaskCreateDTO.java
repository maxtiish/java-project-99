package hexlet.code.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class TaskCreateDTO {
    private Long assigneeId;

    private Integer index;

    private String status;

    @NotBlank
    @Size(min = 1)
    private String title; //name

    private String content; //description

    private Set<Long> taskLabelIds;
}
