package com.project.reflash.backend.dto;

import com.project.reflash.backend.entity.Deck;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeckTeacherDto {
    private Integer deckId;
    private String deckName;
    private String deckDescription;
    private Integer cardCount;

    public DeckTeacherDto(Deck deck, Integer cardCount) {
        this.deckId = deck.getId();
        this.deckName = deck.getName();
        this.deckDescription = deck.getDescription();
        this.cardCount = cardCount;
    }
}
