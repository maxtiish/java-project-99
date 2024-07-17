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
import java.util.Set;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@Table(name = "labels")
@EntityListeners(AuditingEntityListener.class)
@ToString(includeFieldNames = true, onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Label {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    @NotNull
    @ToString.Include
    @Column(unique = true)
    @Size(min = 3, max = 1000)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "labels", cascade = CascadeType.MERGE)
    private Set<Task> tasks;

    @CreatedDate
    private LocalDate createdAt;
}
