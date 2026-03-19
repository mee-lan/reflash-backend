package com.project.reflash.backend.service;

import com.project.reflash.backend.dto.*;
import com.project.reflash.backend.entity.Course;
import com.project.reflash.backend.entity.Deck;
import com.project.reflash.backend.entity.Note;
import com.project.reflash.backend.repository.CourseRepository;
import com.project.reflash.backend.repository.DeckRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DeckService {

    DeckRepository deckRepository;
    CourseRepository courseRepository;

    public DeckService(DeckRepository deckRepository, CourseRepository courseRepository) {
        this.deckRepository = deckRepository;
        this.courseRepository = courseRepository;
    }

    public List<DeckStudentDto> getDecksofStudent(Integer studentId, Integer courseId) {
        //TODO: check whether the course is accessible to this particular studentId, perhaps it does in the repository, but verify properly
        return deckRepository.getDecksOfStudent(studentId, courseId).stream().map(deck -> {
            Integer cardCount = deck.getNotes() != null ? deck.getNotes().size() : 0;
            return new DeckStudentDto(deck, cardCount);
        }).toList();
    }


    public List<DeckTeacherDto> getDecksOfTeacher(Integer teacherId, Integer courseId) {
        //TODO: check whether the course is accessible to this particular teacherId
        return deckRepository.getDecksOfTeacher(teacherId, courseId).stream().map(
                deck -> {
                    Integer cardCount = deck.getNotes() != null ? deck.getNotes().size() : 0;
                    return new DeckTeacherDto(deck, cardCount);
                }
        ).toList();
    }

    public void createDeck(DeckCreationDto deckCreationDto) {
        Deck deck = new Deck();
        deck.setName(deckCreationDto.getDeckName());
        deck.setDescription(deckCreationDto.getDeckDescription());
        Optional<Course> course = courseRepository.findById(deckCreationDto.getCourseId());
        if (course.isEmpty()) {
            throw new RuntimeException("Submitted Course ID does not exist");
        }

        deck.setCourse(course.get());
        deckRepository.save(deck);
    }


    @PreAuthorize("hasRole('TEACHER')")
    public DeckEditDto getDeckForEdit(Integer deckId, Integer userId) {
        Deck deck = deckRepository.getDeckByIdIfAccessibleByTeacher(deckId, userId)
                .orElseThrow(() -> new RuntimeException("Deck is not  accessible to the teacher"));

        DeckEditDto deckEditDto = new DeckEditDto(deck);

        return deckEditDto;
    }


    @Transactional
    @PreAuthorize("hasRole('TEACHER')")
    public void resaveDeckWithNewData(DeckEditDto dto, Integer userId) {
        Deck deck = deckRepository.getDeckByIdIfAccessibleByTeacher(dto.getDeckId(), userId)
                .orElseThrow(() -> new RuntimeException("Deck not found or access denied"));

        deck.setName(dto.getDeckName());
        deck.setDescription(dto.getDeckDescription());

        // 1. Handle Deletions & Updates
        // We create a map of notes that HAVE IDs (existing notes)
        Map<Integer, NoteEditDto> existingNoteUpdates = dto.getNotes().stream()
                .filter(n -> n.getNoteId() != null)
                .collect(Collectors.toMap(NoteEditDto::getNoteId, n -> n));

        // Remove notes from the deck that aren't in the DTO
        deck.getNotes().removeIf(note -> !existingNoteUpdates.containsKey(note.getId()));

        // Update the remaining existing notes
        deck.getNotes().forEach(note -> {
            NoteEditDto update = existingNoteUpdates.get(note.getId());
            updateNoteFields(note, update); // Extracted to a small helper method
        });

        // 2. Handle Creations
        // Find notes in the DTO that DON'T have an ID and add them
        dto.getNotes().stream()
                .filter(n -> n.getNoteId() == null)
                .forEach(newNoteDto -> {
                    Note newNote = new Note();
                    updateNoteFields(newNote, newNoteDto);
                    newNote.setDeck(deck); // Set the relationship
                    deck.getNotes().add(newNote);
                });

        // No save() needed thanks to @Transactional!
    }

    private void updateNoteFields(Note note, NoteEditDto dto) {
        note.setFront(dto.getFront());
        note.setBack(dto.getBack());
        note.setAdditionalContext(dto.getAdditionalContext());
        note.setTags(dto.getTags() != null ? dto.getTags() : new ArrayList<>());
    }
}
