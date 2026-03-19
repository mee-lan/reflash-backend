package com.project.reflash.backend.dto;

import com.project.reflash.backend.entity.Deck;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FlashcardsCollectionDto {
    private Integer deckId;
    private String courseName;
    private String deckName;
    private List<FlashcardDto> flashcards;
    private Long crt;

    public FlashcardsCollectionDto(Deck deck, List<FlashcardDto> flashcards) {
        this.deckId = deck.getId();
        this.deckName = deck.getName();
        this.courseName = deck.getCourse().getName();
        this.flashcards = flashcards;
        this.crt = deck.getCrt();
    }
}
