package hexlet.code.controller;


import hexlet.code.dto.label.LabelCreateDTO;
import hexlet.code.dto.label.LabelDTO;
import hexlet.code.dto.label.LabelUpdateDTO;

import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.repository.LabelRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LabelsController {
    @Autowired
    private LabelMapper mapper;

    @Autowired
    private LabelRepository repository;

    @GetMapping("/labels")
    @ResponseStatus(HttpStatus.OK)
    public List<LabelDTO> index() {
        return repository.findAll().stream()
                .map(l -> mapper.map(l))
                .toList();
    }

    @GetMapping("/labels/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LabelDTO show(@PathVariable Long id) {
        var label = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label with id " + id + " not found"));
        return mapper.map(label);
    }

    @PutMapping("/labels/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LabelDTO update(@PathVariable Long id, @RequestBody @Valid LabelUpdateDTO dto) {
        var label = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label with id " + id + " not found"));
        mapper.update(dto, label);
        repository.save(label);
        return mapper.map(label);
    }

    @PostMapping("/labels")
    @ResponseStatus(HttpStatus.CREATED)
    public LabelDTO create(@RequestBody @Valid LabelCreateDTO dto) {
        var label = mapper.map(dto);
        repository.save(label);
        return mapper.map(label);
    }

    @DeleteMapping("/labels/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
