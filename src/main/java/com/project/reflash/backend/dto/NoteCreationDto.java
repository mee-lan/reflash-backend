package com.project.reflash.backend.dto;


import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class NoteCreationDto {
    private Integer deckId;
    private String front;
    private String back;
    private String additionalContext;
    private List<String> tags;
}
