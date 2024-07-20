package hexlet.code.service;

import hexlet.code.dto.user.UserCreateDTO;
import hexlet.code.dto.user.UserDTO;
import hexlet.code.dto.user.UserUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.UserMapper;
import hexlet.code.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    @Autowired
    private UserMapper mapper;

    public List<UserDTO> getAll() {
        var users = repository.findAll();
        return users.stream()
                .map(u -> mapper.map(u))
                .toList();
    }

    public UserDTO show(Long id) {
        var user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
        return mapper.map(user);
    }

    public UserDTO update(UserUpdateDTO dto, Long id) {
        var user = repository.findById(id)
                .orElseThrow();
        mapper.update(dto, user);
        repository.save(user);
        return mapper.map(user);
    }

    public UserDTO create(UserCreateDTO dto) {
        var user = mapper.map(dto);
        repository.save(user);
        return mapper.map(user);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
