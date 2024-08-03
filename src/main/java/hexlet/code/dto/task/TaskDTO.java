package hexlet.code.dto.task;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

import java.time.LocalDate;

@Getter
@Setter
public class TaskDTO {
    private Long id;

    private Integer index;

    private Long assigneeId;

    private String name;

    private String description;

    private String status;

    private Set<Long> taskLabelIds = new HashSet<>();

    private LocalDate createdAt;
}
