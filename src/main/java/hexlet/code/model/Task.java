package hexlet.code.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Setter;
import lombok.Getter;
import lombok.ToString;
import lombok.EqualsAndHashCode;
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

    @ToString.Include
    @ManyToOne(fetch = FetchType.EAGER)
    private User assignee;

    @NotNull
    @ToString.Include
    @Size(min = 1)
    private String name;

    @ToString.Include
    private String description;

    @NotNull
    @ToString.Include
    @ManyToOne(fetch = FetchType.EAGER)
    private TaskStatus status;

    @ManyToMany(fetch = FetchType.EAGER)
    @ToString.Include
    private Set<Label> labels;

    @CreatedDate
    private LocalDate createdAt;
}
