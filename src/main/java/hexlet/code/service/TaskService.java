package hexlet.code.service;

import hexlet.code.dto.task.TaskCreateDTO;
import hexlet.code.dto.task.TaskDTO;
import hexlet.code.dto.task.TaskUpdateDTO;
import hexlet.code.dto.task.TaskParamsDTO;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.repository.TaskRepository;
import hexlet.code.specification.TaskSpecification;
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
    private TaskMapper mapper;

    public List<TaskDTO> getAll(TaskParamsDTO params) {
        var spec = builder.build(params);
        return repository.findAll(spec).stream()
                .map(mapper::map)
                .toList();
    }

    public TaskDTO getById(Long id) {
        var task = repository.findById(id)
                .orElseThrow();
        return mapper.map(task);
    }

    public TaskDTO create(TaskCreateDTO dto) {
        var task = mapper.map(dto);
        repository.save(task);
        return mapper.map(task);
    }

    public TaskDTO update(TaskUpdateDTO dto, Long id) {
        var task = repository.findById(id)
                .orElseThrow();
        mapper.update(dto, task);
        repository.save(task);
        return mapper.map(task);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
