package com.project.reflash.backend.dto;

import com.project.reflash.backend.entity.Deck;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DeckEditDto {
    private Integer deckId;
    private String deckName;
    private String deckDescription;
    private List<NoteEditDto> notes;

    public DeckEditDto(Deck deck) {
        this.deckId = deck.getId();
        this.deckName = deck.getName();
        this.deckDescription = deck.getDescription();
        this.notes = deck.getNotes().stream().map(NoteEditDto::new).toList();
    }
}
