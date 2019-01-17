package com.kundan.noteit.start.db;

import com.kundan.noteit.start.model.Note;
import com.kundan.noteit.start.model.Notebook;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "noteit.db.recreate", havingValue = "true")
public class DBSeeder implements CommandLineRunner {

    private NoteRepository noteRepository;
    private NotebookRepository notebookRepository;

    public DBSeeder(NoteRepository noteRepository, NotebookRepository notebookRepository) {
        this.noteRepository = noteRepository;
        this.notebookRepository = notebookRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        this.noteRepository.deleteAll();
        this.notebookRepository.deleteAll();

        Notebook defaultNotebook = new Notebook("Default");
        Notebook quotesNotebook = new Notebook("Quotes");

        this.notebookRepository.save(defaultNotebook);
        this.notebookRepository.save(quotesNotebook);

        Note welcomeNote = new Note("Hello", "Welcome to NoteIt !", defaultNotebook);
        Note aQuote = new Note("Latin Quote", "Carpe Diem !", quotesNotebook);

        this.noteRepository.save(welcomeNote);
        this.noteRepository.save(aQuote);

        System.out.println("Initialized Database.");
    }
}
