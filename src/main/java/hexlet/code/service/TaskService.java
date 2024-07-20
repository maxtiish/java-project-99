package hexlet.code.service;

import hexlet.code.dto.task.TaskCreateDTO;
import hexlet.code.dto.task.TaskDTO;
import hexlet.code.dto.task.TaskUpdateDTO;
import hexlet.code.dto.task.TaskParamsDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.repository.TaskRepository;
import hexlet.code.specification.TaskSpecification;
import hexlet.code.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskRepository repository;

    @Autowired
    private TaskSpecification builder;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private TaskMapper mapper;

    public List<TaskDTO> getAll(TaskParamsDTO params) {
        var spec = builder.build(params);
        return repository.findAll(spec).stream()
                .map(mapper::map)
                .toList();
    }

    public TaskDTO show(Long id) {
        var task = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));
        return mapper.map(task);
    }

    public TaskDTO create(TaskCreateDTO dto) {
        var task = mapper.map(dto);
        task.setAssignee(userUtils.getCurrentUser());
        repository.save(task);
        return mapper.map(task);
    }

    public TaskDTO update(TaskUpdateDTO dto, Long id) {
        var task = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));
        mapper.update(dto, task);
        repository.save(task);
        return mapper.map(task);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
