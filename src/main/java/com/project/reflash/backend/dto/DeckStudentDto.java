package com.project.reflash.backend.dto;

import com.project.reflash.backend.entity.Deck;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeckStudentDto {
    private Integer id;
    private String name;

    public DeckStudentDto(Deck deck) {
        this.id = deck.getId();
        this.name = deck.getName();
    }
}
