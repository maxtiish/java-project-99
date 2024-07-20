package hexlet.code.service;

import hexlet.code.dto.label.LabelCreateDTO;
import hexlet.code.dto.label.LabelDTO;
import hexlet.code.dto.label.LabelUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.repository.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelService {
    @Autowired
    private LabelMapper mapper;

    @Autowired
    private LabelRepository repository;

    public List<LabelDTO> getAll() {
        return repository.findAll().stream()
                .map(l -> mapper.map(l))
                .toList();
    }

    public LabelDTO show(Long id) {
        var label = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label with id " + id + " not found"));
        return mapper.map(label);
    }

    public LabelDTO update(LabelUpdateDTO dto, Long id) {
        var label = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label with id " + id + " not found"));
        mapper.update(dto, label);
        repository.save(label);
        return mapper.map(label);
    }

    public LabelDTO create(LabelCreateDTO dto) {
        var label = mapper.map(dto);
        repository.save(label);
        return mapper.map(label);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
