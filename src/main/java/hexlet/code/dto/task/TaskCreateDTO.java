package hexlet.code.dto.task;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class TaskCreateDTO {
    private Long assigneeId;

    private Integer index;

    private String status;

    private String name;

    private String description;

    private Set<Long> taskLabelIds;
}
