package hexlet.code.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Setter
@Getter
@Table(name = "tasks")
@EntityListeners(AuditingEntityListener.class)
@ToString(includeFieldNames = true, onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Task implements BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    @ToString.Include
    private Integer index;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    private User assignee;

    @NotNull
    @ToString.Include
    @Size(min = 1)
    private String name;

    @ToString.Include
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @ToString.Include
    @NotNull
    private TaskStatus status;

    @CreatedDate
    private LocalDate createdAt;
}
