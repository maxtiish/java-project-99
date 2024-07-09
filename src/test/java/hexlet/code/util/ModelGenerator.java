package hexlet.code.util;

import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import net.datafaker.Faker;
import org.instancio.Instancio;
import lombok.Getter;
import org.instancio.Select;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ModelGenerator {
    private static Faker faker = new Faker();

    public static User generateUser() {
        return Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .ignore(Select.field(User::getCreatedAt))
                .ignore(Select.field(User::getUpdatedAt))
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .supply(Select.field(User::getFirstName), () -> faker.name().firstName())
                .supply(Select.field(User::getLastName), () -> faker.name().lastName())
                .supply(Select.field(User::getPasswordDigest), () -> faker.internet().password(3, 10))
                .create();
    }

    public static TaskStatus generateTaskStatus() {
        return Instancio.of((TaskStatus.class))
                .ignore(Select.field(TaskStatus::getId))
                .supply(Select.field(TaskStatus::getName), () -> faker.name().fullName())
                .supply(Select.field(TaskStatus::getSlug), () -> faker.internet().slug())
                .create();
    }
}
