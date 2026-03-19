package com.project.reflash.backend.dto;

import lombok.Getter;

@Getter
public class DeckCreationDto {
    private String deckName;
    private String deckDescription;
    private Integer courseId;
}
