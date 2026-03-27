package com.project.reflash.backend.dto;

import com.project.reflash.backend.entity.Note;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
    private String crtFormatted;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.of("Asia/Kathmandu"));

    public NoteDto(Note note) {
        this.noteId = note.getId();
        this.front = note.getFront();
        this.back = note.getBack();
        this.additionalContext = note.getAdditionalContext();
        this.tags = note.getTags();
        this.crt = note.getCrt();
        this.crtFormatted = FORMATTER.format(Instant.ofEpochSecond(note.getCrt()));
    }
}
