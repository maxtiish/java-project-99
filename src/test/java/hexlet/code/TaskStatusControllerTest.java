package hexlet.code;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.status.TaskStatusCreateDTO;
import hexlet.code.dto.status.TaskStatusUpdateDTO;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.util.ModelGenerator;
import jakarta.servlet.ServletException;
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

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskStatusControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskStatusMapper mapper;

    @Autowired
    private TaskStatusRepository statusRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ObjectMapper om;
    private TaskStatus testTaskStatus;
    private final String testSlug = "word";
    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;

    @BeforeEach
    public void setUp() {
        testTaskStatus = ModelGenerator.generateTaskStatus();
        token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));
        statusRepository.save(testTaskStatus);
    }

    @AfterEach
    public void clean() {
        taskRepository.deleteAll();
        if (testTaskStatus != null) {
            statusRepository.deleteById(testTaskStatus.getId());
        }
    }

    @Test
    public void testGetAll() throws Exception {
        var result = mockMvc.perform(get("/api/task_statuses").with(jwt()))
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();

    }

    @Test
    public void testGetById() throws Exception {
        var request = get("/api/task_statuses/" + testTaskStatus.getId()).with(token);

        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                v -> v.node("name").isEqualTo(testTaskStatus.getName()),
                v -> v.node("slug").isEqualTo(testTaskStatus.getSlug()),
                v -> v.node("createdAt").isPresent()
        );
    }

    @Test
    public void testCreate() throws Exception {
        var dto = new TaskStatusCreateDTO()
                .setName("name")
                .setSlug("slug");

        var request = post("/api/task_statuses")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var taskStatus = statusRepository.findBySlug(testTaskStatus.getSlug());
        assertThat(taskStatus).isNotNull();
        assertEquals("name", dto.getName());
        assertEquals("slug", dto.getSlug());
    }

    @Test
    public void testUpdate() throws Exception {
        var dto = new TaskStatusUpdateDTO();
        dto.setSlug(JsonNullable.of(testSlug));

        var request = put("/api/task_statuses/" + testTaskStatus.getId()).with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));
        mockMvc.perform(request).andExpect(status().isOk());

        var taskStatus = statusRepository.findById(testTaskStatus.getId()).orElseThrow();

        assertThat(taskStatus.getSlug()).isEqualTo(testSlug);
    }

    @Test
    public void testDelete() throws Exception {
        var request = delete("/api/task_statuses/" + testTaskStatus.getId()).with(token);
        mockMvc.perform(request).andExpect(status().isNoContent());
        assertThat(statusRepository.existsById(testTaskStatus.getId())).isEqualTo(false);
    }

    @Test
    public void testDeleteWhileHasTask() {
        var task = ModelGenerator.generateTask();
        task.setTaskStatus(testTaskStatus);
        taskRepository.save(task);

        Throwable thrown = assertThrows(ServletException.class, () -> {
            mockMvc.perform(delete("/api/task_statuses/" + testTaskStatus.getId()).with(token)); });
        assertNotNull(thrown.getMessage());

    }
}
