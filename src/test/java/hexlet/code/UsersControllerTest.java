package hexlet.code;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import hexlet.code.dto.user.UserDTO;
import hexlet.code.mapper.UserMapper;
import hexlet.code.repository.TaskRepository;
import hexlet.code.util.ModelGenerator;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.AfterEach;
import org.openapitools.jackson.nullable.JsonNullable;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class
UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserMapper userMapper;
    private User testUser;
    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;

    @BeforeEach
    public void beforeEach() {
        testUser = ModelGenerator.generateUser();
        userRepository.save(testUser);
        token = jwt().jwt(builder -> builder.subject(testUser.getEmail()));
    }

    @AfterEach
    public void clean() {
        taskRepository.deleteAll();
        if (testUser != null) {
            userRepository.deleteById(testUser.getId());
        }
    }

    @Test
    public void testGetAll() throws Exception {
        mockMvc.perform(get("/api/users").with(token))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetById() throws Exception {
        var request = get("/api/users/" + testUser.getId()).with(token);
        mockMvc.perform(request)
                .andExpect(status().isOk());
    }

    @Test
    void testCreate() throws Exception {
        var data = ModelGenerator.generateUser();

        var request = post("/api/users")
                .with(token)
                .contentType(APPLICATION_JSON)
                .content(om.writeValueAsString(data));
        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andReturn();

        var user = userRepository.findByEmail(data.getEmail()).orElseThrow();
        assertThat(user.getFirstName()).isEqualTo(data.getFirstName());
        assertThat(user.getLastName()).isEqualTo(data.getLastName());
        assertThat(user.getEmail()).isEqualTo(data.getEmail());
        assertThat(user.getCreatedAt()).isNotNull();
        assertThat(user.getId()).isNotNull();
    }

    @Test
    public void testUpdate() throws Exception {
        var data = new UserDTO();
        data.setFirstName(String.valueOf(JsonNullable.of("new name")));

        var request = put("/api/users/" + testUser.getId()).with(token)
                .contentType(APPLICATION_JSON)
                .content(om.writeValueAsString(data));
        mockMvc.perform(request)
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateWithWrongUser() throws Exception {
        testUser.setFirstName(String.valueOf(JsonNullable.of("new name")));

        var wrongUser = ModelGenerator.generateUser();
        var newToken = jwt().jwt(builder -> builder.subject(wrongUser.getEmail()));

        mockMvc.perform(post("/api/users").with(newToken)
                .contentType(APPLICATION_JSON)
                .content(om.writeValueAsString(wrongUser)));

        var request = put("/api/users/" + testUser.getId()).with(newToken)
                .contentType(APPLICATION_JSON)
                .content(om.writeValueAsString(testUser));
        mockMvc.perform(request)
                .andExpect(status().isForbidden());
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete("/api/users/" + testUser.getId()).with(token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteWithWrongUser() throws Exception {
        var user = ModelGenerator.generateUser();
        mockMvc.perform(post("/api/users").with(jwt())
                .contentType(APPLICATION_JSON)
                .content(om.writeValueAsString(user)));
        var newToken = jwt().jwt(builder -> builder.subject(user.getEmail()));

        mockMvc.perform(delete("/api/users/" + testUser.getId()).with(newToken))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testDeleteWhileHasTask() {
        var task = ModelGenerator.generateTask();
        task.setAssignee(testUser);
        taskRepository.save(task);
        Throwable thrown = assertThrows(ServletException.class, () -> {
            mockMvc.perform(delete("/api/users/" + testUser.getId()).with(token)); });
        assertNotNull(thrown.getMessage());
    }
}
