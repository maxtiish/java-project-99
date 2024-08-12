package hexlet.code.dto.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("assignee_id")
    private Long assigneeId;

    private String title; //name

    private String content; //description

    private String status;

    private Set<Long> taskLabelIds = new HashSet<>();

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdAt;
}
