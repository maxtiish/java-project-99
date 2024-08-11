package hexlet.code.service;

import hexlet.code.dto.status.TaskStatusCreateDTO;
import hexlet.code.dto.status.TaskStatusDTO;
import hexlet.code.dto.status.TaskStatusUpdateDTO;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskStatusService {
    private TaskStatusRepository statusRepository;
    private TaskRepository taskRepository;
    private TaskStatusMapper mapper;

    public List<TaskStatusDTO> getAll() {
        var taskStatuses = statusRepository.findAll();
        return taskStatuses.stream()
                .map(u -> mapper.map(u))
                .toList();
    }

    public TaskStatusDTO getById(Long id) {
        var taskStatus = statusRepository.findById(id)
                .orElseThrow();
        return mapper.map(taskStatus);
    }

    public TaskStatusDTO create(TaskStatusCreateDTO dto) {
        var taskStatus = mapper.map(dto);
        statusRepository.save(taskStatus);
        return mapper.map(taskStatus);
    }

    public TaskStatusDTO update(TaskStatusUpdateDTO dto, Long id) {
        var taskStatus = statusRepository.findById(id)
                .orElseThrow();
        mapper.update(dto, taskStatus);
        statusRepository.save(taskStatus);
        return mapper.map(taskStatus);
    }

    public void delete(Long id) {
        var status = statusRepository.findById(id);
        if (status.isPresent() && taskRepository.findByTaskStatusName(status.get().getName()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can Not Delete While Status Has Task");
        }
        statusRepository.deleteById(id);
    }
}
