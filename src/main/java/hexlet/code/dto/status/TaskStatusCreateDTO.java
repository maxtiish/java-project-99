package hexlet.code.dto.status;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TaskStatusCreateDTO {
    @NotBlank
    @Size(min = 1)
    private String name;

    @Column(unique = true)
    @NotBlank
    private String slug;

    private LocalDate createdAt;
}
