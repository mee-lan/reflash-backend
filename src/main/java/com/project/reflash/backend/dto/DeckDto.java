package com.project.reflash.backend.dto;

import com.project.reflash.backend.entity.Deck;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DeckDto {
    private Integer deckId;
    private String courseName;
    private String deckName;
    private List<FlashcardDto> flashcards;
    private Long crt;

    public DeckDto(Deck deck, List<FlashcardDto> flashcards) {
        this.deckId = deck.getId();
        this.deckName = deck.getName();
        this.courseName = deck.getCourse().getCourseName();
        this.flashcards = flashcards;
        this.crt = deck.getCrt();
    }
}
