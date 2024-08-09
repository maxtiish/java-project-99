package hexlet.code.component;

import hexlet.code.model.Label;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.repository.LabelRepository;
import hexlet.code.service.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {
    private final UserRepository userRepository;
    private final TaskStatusRepository taskStatusRepository;
    private final CustomUserDetailsService userService;
    private final LabelRepository labelRepository;
    private final List<TaskStatus> statuses = createTaskStatuses();
    private final List<Label> labels = createLabels();

    @Override
    public void run(ApplicationArguments args) throws Exception {
        var email = "hexlet@example.com";
        var userData = new User();
        userData.setEmail(email);
        userData.setPasswordDigest("qwerty");

        if (userRepository.findByEmail("hexlet@example.com").isEmpty()) {
            userService.createUser(userData);
        }
        for (var status : statuses) {
            taskStatusRepository.save(status);
        }
        for (var label : labels) {
            labelRepository.save(label);
        }
    }

    private static List<TaskStatus> createTaskStatuses() {
        String[] taskNames = {"draft", "toReview", "toBeFixed", "toPublish", "published"};
        List<TaskStatus> result = new ArrayList<>();

        for (String taskName : taskNames) {
            TaskStatus status = new TaskStatus();
            status.setSlug(taskName);
            status.setName(taskName);
            result.add(status);
        }
        return result;
    }

    private static List<Label> createLabels() {
        String[] names = {"bug", "feature"};
        List<Label> result = new ArrayList<>();

        for (String name : names) {
            Label label = new Label();
            label.setName(name);
            result.add(label);
        }
        return result;
    }
}
