package com.project.reflash.backend.service;

import com.project.reflash.backend.dto.DeckStudentDto;
import com.project.reflash.backend.dto.DeckTeacherDto;
import com.project.reflash.backend.repository.DeckRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class DeckService {

    DeckRepository deckRepository;

    public DeckService(DeckRepository deckRepository) {
        this.deckRepository = deckRepository;
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

}
