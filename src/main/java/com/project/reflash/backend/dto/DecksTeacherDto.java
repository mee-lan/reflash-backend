package com.project.reflash.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DecksTeacherDto {
    List<DeckTeacherDto> decks;
    List<StudentDto> students;
    List<TeacherDto> teachers;

    public DecksTeacherDto(List<DeckTeacherDto> decks, List<StudentDto> students, List<TeacherDto> teachers) {
        this.decks = decks;
        this.students = students;
        this.teachers = teachers;
    }
}
