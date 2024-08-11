package hexlet.code.service;

import hexlet.code.dto.user.UserCreateDTO;
import hexlet.code.dto.user.UserDTO;
import hexlet.code.dto.user.UserUpdateDTO;
import hexlet.code.mapper.UserMapper;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;
    private TaskRepository taskRepository;
    private UserMapper mapper;

    public List<UserDTO> getAll() {
        var users = userRepository.findAll();
        return users.stream()
                .map(u -> mapper.map(u))
                .toList();
    }

    public UserDTO getById(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow();
        return mapper.map(user);
    }

    public UserDTO update(UserUpdateDTO dto, Long id) {
        var user = userRepository.findById(id)
                .orElseThrow();
        mapper.update(dto, user);
        userRepository.save(user);
        return mapper.map(user);
    }

    public UserDTO create(UserCreateDTO dto) {
        var user = mapper.map(dto);
        userRepository.save(user);
        return mapper.map(user);
    }

    public void delete(Long id) {
        var user = userRepository.findById(id);
        if (user.isPresent() && taskRepository.findByAssigneeEmail(user.get().getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can Not Delete While User Has Task");
        } else {
            userRepository.deleteById(id);
        }
    }
}
