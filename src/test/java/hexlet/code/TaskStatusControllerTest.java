package hexlet.code;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.status.TaskStatusCreateDTO;
import hexlet.code.dto.status.TaskStatusUpdateDTO;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
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
    private WebApplicationContext wac;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskStatusMapper mapper;

    @Autowired
    private TaskStatusRepository repository;

    @Autowired
    private ObjectMapper om;

    private ModelGenerator modelGenerator = new ModelGenerator();
    private static Faker faker = new Faker();
    private TaskStatus testTaskStatus;
    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;

    @BeforeEach
    public void setUp() {
        token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));

        testTaskStatus = modelGenerator.generateTaskStatus();
    }

    @AfterEach
    public void clean() {
        repository.deleteAll();
    }

    @Test
    public void testGetAll() throws Exception {
        repository.save(testTaskStatus);
        var result = mockMvc.perform(get("/api/task_statuses").with(jwt()))
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();

    }

    @Test
    public void testCreate() throws Exception {
        var dto = new TaskStatusCreateDTO()
                .name("name")
                .slug("slug");

        var request = post("/api/task_statuses")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var taskStatus = repository.findBySlug(testTaskStatus.getSlug()).orElseThrow();
        assertThat(taskStatus).isNotNull();
        assertEquals("name", dto.name());
        assertEquals("slug", dto.slug());
    }

    @Test
    public void testUpdate() throws Exception {
        repository.save(testTaskStatus);

        var dto = new TaskStatusUpdateDTO();
        String slug = faker.internet().slug();
        dto.setSlug(JsonNullable.of(slug));

        var request = put("/api/task_statuses/" + testTaskStatus.getId())
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));
        mockMvc.perform(request).andExpect(status().isOk());

        var taskStatus = repository.findById(testTaskStatus.getId()).orElseThrow();

        assertThat(taskStatus.getSlug()).isEqualTo(slug);
    }

    @Test
    public void testGetById() throws Exception {
        repository.save(testTaskStatus);

        var request = get("/api/task_statuses/" + testTaskStatus.getId()).with(token);

        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                v -> v.node("name").isEqualTo(testTaskStatus.getName()),
                v -> v.node("slug").isEqualTo(testTaskStatus.getSlug())
        );
    }

    @Test
    public void testDelete() throws Exception {
        repository.save(testTaskStatus);
        var request = delete("/api/task_statuses/" + testTaskStatus.getId()).with(token);
        mockMvc.perform(request).andExpect(status().isNoContent());
        assertThat(repository.existsById(testTaskStatus.getId())).isEqualTo(false);
    }
}
