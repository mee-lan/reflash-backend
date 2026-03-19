package com.project.reflash.backend.dto;

import com.project.reflash.backend.entity.Note;
import lombok.Getter;

import java.util.List;

@Getter
public class NoteEditDto {
    private final Integer noteId;
    private final String front;
    private final String back;
    private final String additionalContext;
    private final List<String> tags;

    public NoteEditDto(Note note) {
        this.noteId = note.getId();
        this.front = note.getFront();
        this.back = note.getBack();
        this.additionalContext = note.getAdditionalContext();
        this.tags = note.getTags();
    }
}
