package com.kundan.noteit.start;

import com.kundan.noteit.start.api.ViewController.NoteViewModel;
import com.kundan.noteit.start.api.ViewController.NotebookViewModel;
import com.kundan.noteit.start.db.NotebookRepository;
import com.kundan.noteit.start.model.Note;
import com.kundan.noteit.start.model.Notebook;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Map entity <-> view model
 */
@Component
public class Mapper {

    private NotebookRepository notebookRepository;

    public Mapper(NotebookRepository notebookRepository) {
        this.notebookRepository = notebookRepository;
    }

    public NoteViewModel convertToNoteViewModel(Note entity) {
        NoteViewModel viewModel = new NoteViewModel();
        viewModel.setId(entity.getId().toString());
        viewModel.setTitle(entity.getTitle());
        viewModel.setText(entity.getText());
        viewModel.setNotebookId(entity.getNotebook().getId().toString());
        viewModel.setLastModifiedOn(entity.getLastModifiedTime());

        return viewModel;
    }

    public NotebookViewModel convertToNotebookViewModel(Notebook entity) {
        NotebookViewModel viewModel = new NotebookViewModel();
        viewModel.setId(entity.getId().toString());
        viewModel.setName(entity.getName());
        viewModel.setNbNotes(entity.getNotes().size());

        return viewModel;
    }

    public Note convertToNoteEntity(NoteViewModel viewModel) {

        Notebook notebook = this.notebookRepository.findById(UUID.fromString(viewModel.getNotebookId())).get();

        return new Note(viewModel.getId(), viewModel.getTitle(), viewModel.getText(), notebook);
    }

    public Notebook convertToNotebookEntity(NotebookViewModel viewModel) {
        return new Notebook(viewModel.getId(), viewModel.getName());
    }
}
