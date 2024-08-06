package hexlet.code;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.task.TaskUpdateDTO;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.util.ModelGenerator;
import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {
    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaskMapper mapper;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    private ModelGenerator modelGenerator = new ModelGenerator();
    private static Faker faker = new Faker();

    private Task testTask;
    private User user;
    private TaskStatus status;

    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;

    @BeforeEach
    public void setUp() {
        user = modelGenerator.generateUser();
        userRepository.save(user);
        token = jwt().jwt(builder -> builder.subject(user.getEmail()));

        status = modelGenerator.generateTaskStatus();
        taskStatusRepository.save(status);
        testTask = modelGenerator.generateTask();

        testTask.setAssignee(user);
        testTask.setTaskStatus(status);
    }

    @AfterEach
    public void clean() {
        taskRepository.deleteAll();
        taskStatusRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testGetAll() throws Exception {
        taskRepository.save(testTask);
        var result = mockMvc.perform(get("/api/tasks").with(jwt()))
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testGetById() throws Exception {
        taskRepository.save(testTask);

        var request = get("/api/tasks/" + testTask.getId()).with(token);

        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                v -> v.node("name").isEqualTo(testTask.getName()),
                v -> v.node("createdAt").isPresent(),
                v -> v.node("description").isEqualTo(testTask.getDescription()),
                v -> v.node("index").isEqualTo(testTask.getIndex())
        );
    }

    @Test
    public void testCreate() throws Exception {
        var dto = mapper.map(testTask);

        var request = post("/api/tasks")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));
        mockMvc.perform(request).andExpect(status().isCreated());

        var task = taskRepository.findByName(testTask.getName()).orElseThrow();
        assertNotNull(task);
        assertThat(task.getName()).isEqualTo(dto.getName());
        assertThat(task.getDescription()).isEqualTo(dto.getDescription());
        assertThat(task.getCreatedAt()).isNotNull();
    }

    @Test
    public void testUpdate() throws Exception {
        taskRepository.save(testTask);
        var dto = new TaskUpdateDTO();
        dto.setName(JsonNullable.of("name"));
        dto.setDescription(JsonNullable.of("description"));

        var request = put("/api/tasks/" + testTask.getId()).with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));
        mockMvc.perform(request).andExpect(status().isOk());

        var task = taskRepository.findById(testTask.getId()).orElseThrow();
        assertThat(task.getName()).isEqualTo(dto.getName().get());
        assertThat(task.getDescription()).isEqualTo(dto.getDescription().get());
    }

    @Test
    public void testDelete() throws Exception {
        taskRepository.save(testTask);
        var request = delete("/api/tasks/" + testTask.getId()).with(token);
        mockMvc.perform(request).andExpect(status().isNoContent());
        assertThat(taskRepository.existsById(testTask.getId())).isEqualTo(false);
    }

    @Test
    public void testParams() throws Exception {
        var request = get("/api/tasks?titleCont=create&assigneeId=1&taskStatus=to_be_fixed&labelId=1").with(token);
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray().allSatisfy(el ->
                assertThatJson(el)
                        .and(v -> v.node("name").asString().containsIgnoringCase("create"))
                        .and(v -> v.node("assigneeId").isEqualTo(1))
                        .and(v -> v.node("labelId").isEqualTo(1))
                        .and(v -> v.node("taskStatus").asString().isEqualTo("to_be_fixed"))
        );
    }
}
