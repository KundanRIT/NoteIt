package com.kundan.noteit.start.api;

import com.kundan.noteit.start.Mapper;
import com.kundan.noteit.start.api.ViewController.NotebookViewModel;
import com.kundan.noteit.start.db.NotebookRepository;
import com.kundan.noteit.start.model.Notebook;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.ValidationException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api/notebooks")
public class NotebookController {
    private NotebookRepository notebookRepository;
    private Mapper mapper;

    public NotebookController(NotebookRepository notebookRepository, Mapper mapper) {
        this.notebookRepository = notebookRepository;
        this.mapper = mapper;
    }

    @GetMapping("/all")
    public List<NotebookViewModel> all() {
        List<Notebook> notebooks = this.notebookRepository.findAll();
        List<NotebookViewModel> viewModels = notebooks.stream()
                .map(notebook -> this.mapper.convertToNotebookViewModel(notebook))
                .collect(Collectors.toList());
        return viewModels;
    }

    @PostMapping
    public Notebook save(@RequestBody NotebookViewModel notebookViewModel, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException();
        }
        Notebook note = this.mapper.convertToNotebookEntity(notebookViewModel);
        return this.notebookRepository.save(note);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable String id) {
        this.notebookRepository.deleteById(UUID.fromString(id));
    }
}