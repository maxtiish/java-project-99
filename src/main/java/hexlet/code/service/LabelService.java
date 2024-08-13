package hexlet.code.service;

import hexlet.code.dto.label.LabelCreateDTO;
import hexlet.code.dto.label.LabelDTO;
import hexlet.code.dto.label.LabelUpdateDTO;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LabelService {
    private LabelMapper mapper;
    private LabelRepository labelRepository;
    private TaskRepository taskRepository;

    public List<LabelDTO> getAll() {
        return labelRepository.findAll().stream()
                .map(l -> mapper.map(l))
                .toList();
    }

    public LabelDTO getById(Long id) {
        var label = labelRepository.findById(id)
                .orElseThrow();
        return mapper.map(label);
    }

    public LabelDTO update(LabelUpdateDTO dto, Long id) {
        var label = labelRepository.findById(id)
                .orElseThrow();
        mapper.update(dto, label);
        labelRepository.save(label);
        return mapper.map(label);
    }

    public LabelDTO create(LabelCreateDTO dto) {
        var label = mapper.map(dto);
        labelRepository.save(label);
        return mapper.map(label);
    }

    public void delete(Long id) {
        labelRepository.deleteById(id);
    }
}
