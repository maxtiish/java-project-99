package hexlet.code.specification;

import hexlet.code.dto.task.TaskParamsDTO;
import hexlet.code.model.Task;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TaskSpecification {
    public Specification<Task> build(TaskParamsDTO params) {
        return withTitleCont(params.getTitleCont())
                .and(withAssigneeId(params.getAssigneeId()))
                .and(withStatus(params.getStatus()))
                .and(withLabelId(params.getLabelId()));
    }

    public Specification<Task> withTitleCont(String substring) {
        return (root, query, cb) -> substring == null
                ? cb.conjunction()
                : cb.like(cb.lower(root.get("name")), "%" + substring.toLowerCase() + "%");
    }

    public Specification<Task> withAssigneeId(Long id) {
        return (root, query, cb) -> id == null
                ? cb.conjunction()
                : cb.equal(root.get("assignee").get("id"), id);
    }

    public Specification<Task> withStatus(String slug) {
        return (root, query, cb) -> slug == null
                ? cb.conjunction()
                : cb.equal(root.get("status").get("slug"), slug);
    }

    public Specification<Task> withLabelId(Long id) {
        return (root, query, cb) -> id == null
                ? cb.conjunction()
                : cb.equal(root.get("labels").get("id"), id);
    }
}
