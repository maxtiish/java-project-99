package hexlet.code.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Setter;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.Set;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Setter
@Getter
@Table(name = "tasks")
@EntityListeners(AuditingEntityListener.class)
public class Task implements BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Integer index;

    @ManyToOne(fetch = FetchType.EAGER)
    private User assignee;

    @NotBlank
    @Size(min = 1)
    private String name;

    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    private TaskStatus taskStatus;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Label> labels;

    @CreatedDate
    private LocalDate createdAt;
}
