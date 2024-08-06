package hexlet.code.dto.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class TaskCreateDTO {
    @JsonProperty("assignee_id")
    private Long assigneeId;

    private Integer index;

    @NotNull
    private String status;

    @NotBlank
    private String title; //name

    private String content; //description

    private Set<Long> taskLabelIds;

    private LocalDate createdAt;
}
