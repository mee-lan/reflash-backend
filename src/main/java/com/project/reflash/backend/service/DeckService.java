package com.project.reflash.backend.service;

import com.project.reflash.backend.dto.DeckCreationDto;
import com.project.reflash.backend.dto.DeckEditDto;
import com.project.reflash.backend.dto.DeckStudentDto;
import com.project.reflash.backend.dto.DeckTeacherDto;
import com.project.reflash.backend.entity.Course;
import com.project.reflash.backend.entity.Deck;
import com.project.reflash.backend.repository.CourseRepository;
import com.project.reflash.backend.repository.DeckRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
}
