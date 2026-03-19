package com.project.reflash.backend.dto;

import com.project.reflash.backend.entity.Deck;
import lombok.Getter;

import java.util.List;

@Getter
public class DeckEditDto {
    private final Integer deckId;
    private final String deckName;
    private final String deckDescription;
    private final List<NoteEditDto> notes;

    public DeckEditDto(Deck deck) {
        this.deckId = deck.getId();
        this.deckName = deck.getName();
        this.deckDescription = deck.getDescription();
        this.notes = deck.getNotes().stream().map(NoteEditDto::new).toList();
    }
}
