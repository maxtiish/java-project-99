package hexlet.code.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "task_statuses")
@EntityListeners(AuditingEntityListener.class)
@ToString(includeFieldNames = true, onlyExplicitlyIncluded = true)
@Getter
@Setter
@EqualsAndHashCode
public class TaskStatus implements BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    @NotNull
    @Column(unique = true)
    @ToString.Include
    @Size(min = 1)
    private String name;

    @Column(unique = true)
    @ToString.Include
    @Size(min = 1)
    private String slug;

    @OneToMany(mappedBy = "status", cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private List<Task> tasks;

    @CreatedDate
    private LocalDate createdAt;
}
