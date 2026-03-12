package com.project.reflash.backend.dto;

import com.project.reflash.backend.entity.Note;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NoteDto {
    private Integer id;
    private String front;
    private String back;
    private String additionalContext;
    private List<String> tags;

    public NoteDto(Note note) {
        this.id = note.getId();
        this.front = note.getFront();
        this.back = note.getBack();
        this.additionalContext = note.getAdditionalContext();
        this.tags = note.getTags();
    }
}
