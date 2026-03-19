package com.project.reflash.backend.dto;

import com.project.reflash.backend.entity.Note;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class NoteDto {
    private Integer noteId;
    private String front;
    private String back;
    private String additionalContext;
    private List<String> tags;
    private Long crt;

    public NoteDto(Note note) {
        this.noteId = note.getId();
        this.front = note.getFront();
        this.back = note.getBack();
        this.additionalContext = note.getAdditionalContext();
        this.tags = note.getTags();
        this.crt = note.getCrt();
    }
}
