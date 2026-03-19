package com.project.reflash.backend.service;

import com.project.reflash.backend.dto.NoteCreationDto;
import com.project.reflash.backend.dto.NoteDto;
import com.project.reflash.backend.entity.Deck;
import com.project.reflash.backend.entity.Note;
import com.project.reflash.backend.repository.DeckRepository;
import com.project.reflash.backend.repository.NoteRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
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


    public List<NoteDto> getNotesForADeckTeacher(Integer deckId, Integer userId) {
        //TODO: verifies that the deck is accessible to the teacher
        Deck deck = deckRepository.getDeckByIdIfAccessibleByTeacher(deckId, userId).orElseThrow(() -> new RuntimeException("Deck is not accessible"));

        List<Note> notes = deck.getNotes();
        List<NoteDto> notesDto = notes.stream().map(NoteDto::new).toList();

        return notesDto;
    }


    @PreAuthorize("hasRole('TEACHER')")
    public List<String> getQuestionsFromDeck(List<Integer> deckIds, Integer count, Integer userId) {

        //TODO: optimize this so that database isn't hit several times
        List<Deck> decks = new ArrayList<>();
        for(Integer deckId: deckIds) {
            decks.add(deckRepository.getDeckByIdIfAccessibleByTeacher(deckId, userId).orElseThrow(() -> new RuntimeException("Deck is not accessible")));
        }

        List<String> questions = new ArrayList<>();

        for(Deck deck: decks) {
            questions.addAll(deck.getNotes().stream().map(Note::getFront).toList());
        }

        if(questions.size() <= count) {
            return questions;
        }


        Collections.shuffle(questions); // randomly shuffle the list
        return questions.subList(0, count); // pick first 'count' elements
    }
}
