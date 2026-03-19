package com.project.reflash.backend.service;

import com.project.reflash.backend.dto.NoteCreationDto;
import com.project.reflash.backend.entity.Deck;
import com.project.reflash.backend.entity.Note;
import com.project.reflash.backend.repository.DeckRepository;
import com.project.reflash.backend.repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NoteService {
    private final NoteRepository noteRepository;
    private final DeckRepository deckRepository;

    public NoteService(NoteRepository noteRepository, DeckRepository deckRepository) {
        this.noteRepository = noteRepository;
        this.deckRepository = deckRepository;
    }

    public void createNote(NoteCreationDto noteCreationDto) {

        Note note = getNoteFromDto(noteCreationDto);
        noteRepository.save(note);
    }

    public void createNotes(List<NoteCreationDto> noteCreationDtos) {
        List<Note> notes = noteCreationDtos.stream().map(this::getNoteFromDto).toList();
        noteRepository.saveAll(notes);
    }

    private Note getNoteFromDto(NoteCreationDto noteCreationDto) {
        //TODO: verify this method properly
        Deck deck = deckRepository.findById(noteCreationDto.getDeckId())
                .orElseThrow(() -> new RuntimeException("Deck not found with id: " + noteCreationDto.getDeckId()));
        Note note = new Note();

        note.setFront(noteCreationDto.getFront());
        note.setBack(noteCreationDto.getBack());
        note.setDeck(deck);
        note.setAdditionalContext(noteCreationDto.getAdditionalContext());
        note.setTags(noteCreationDto.getTags() != null ? noteCreationDto.getTags() : new ArrayList<>());

        return note;
    }
}
