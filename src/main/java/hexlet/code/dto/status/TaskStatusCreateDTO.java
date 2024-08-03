package hexlet.code.dto.status;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Getter
@Setter
@Accessors(chain = true)
public class TaskStatusCreateDTO {
    @NotBlank
    @Size(min = 1)
    private String name;

    @Column(unique = true)
    @NotBlank
    private String slug;

    private LocalDate createdAt;
}
