package com.kundan.noteit.start.api;

import com.kundan.noteit.start.Mapper;
import com.kundan.noteit.start.api.ViewController.NoteViewModel;
import com.kundan.noteit.start.db.NoteRepository;
import com.kundan.noteit.start.db.NotebookRepository;
import com.kundan.noteit.start.model.Note;
import com.kundan.noteit.start.model.Notebook;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api/notes")
public class NoteController {
    private NoteRepository noteRepository;
    private NotebookRepository notebookRepository;
    private Mapper mapper;

    public NoteController(NoteRepository noteRepository, NotebookRepository notebookRepository, Mapper mapper) {
        this.noteRepository = noteRepository;
        this.notebookRepository = notebookRepository;
        this.mapper = mapper;
    }

    @GetMapping("/all")
    public List<NoteViewModel> all() {
        List<Note> notes = this.noteRepository.findAll();
        List<NoteViewModel> viewModels = notes.stream()
                .map(note -> this.mapper.convertToNoteViewModel(note))
                .collect(Collectors.toList());
        return viewModels;
    }

    @GetMapping("/{id}")
    public NoteViewModel byId(@PathVariable String id) {
        Note note = this.noteRepository.findById(UUID.fromString(id)).orElse(null);
        if (note == null) {
            throw new EntityNotFoundException();
        }

        NoteViewModel viewModel = this.mapper.convertToNoteViewModel(note);
        return viewModel;
    }


    @GetMapping("/notebook/{notebookId}")
    public List<NoteViewModel> byNotebookId(@PathVariable String notebookId) {
        List<Note> notes = new ArrayList<>();

        Optional<Notebook> notebook = this.notebookRepository.findById(UUID.fromString(notebookId));
        if (notebook.isPresent()) {
            notes = this.noteRepository.findAllByNotebook(notebook.get());
        }
        List<NoteViewModel> viewModels = notes.stream()
                .map(note -> this.mapper.convertToNoteViewModel(note))
                .collect(Collectors.toList());
        return viewModels;
    }

    @PostMapping
    public Note save(@RequestBody NoteViewModel noteViewModel, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException();
        }
        Note note = this.mapper.convertToNoteEntity(noteViewModel);
        return this.noteRepository.save(note);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable String id) {
        this.noteRepository.deleteById(UUID.fromString(id));
    }
}
