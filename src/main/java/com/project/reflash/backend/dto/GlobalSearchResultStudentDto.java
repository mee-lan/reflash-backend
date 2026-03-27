package com.project.reflash.backend.dto;

import com.project.reflash.backend.entity.Course;
import com.project.reflash.backend.entity.Deck;
import com.project.reflash.backend.entity.Note;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.poi.sl.usermodel.Notes;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class GlobalSearchResultStudentDto {
    List<CourseStudentDto> courses = new ArrayList<>();
    List<DeckStudentDto> decks = new ArrayList<>();
    List<NoteDto> notes = new ArrayList<>();

    public GlobalSearchResultStudentDto(List<Course> courses, List<Deck> decks, List<Note> notes) {
        this.courses = courses.stream().map(CourseStudentDto::new).toList();
        this.decks = decks.stream().map(DeckStudentDto::new).toList();
        this.notes = notes.stream().map(NoteDto::new).toList();
    }
}
