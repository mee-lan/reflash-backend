package com.project.reflash.backend.dto;

import com.project.reflash.backend.algorithm.CardQueue;
import com.project.reflash.backend.algorithm.CardType;
import com.project.reflash.backend.entity.Flashcard;
import com.project.reflash.backend.entity.Note;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlashcardDto {
    private Integer id;
    private NoteDto note;
    private Long crt;
    private CardType type;
    private CardQueue queue;
    private Integer ivl;
    private Integer factor;
    private Integer reps;
    private Integer lapses;
    private Integer left;
    private Long due;

    public FlashcardDto(Flashcard flashcard) {
        this.id = flashcard.getId();
        this.note = new NoteDto(flashcard.getNote());
        this.crt = flashcard.getCrt();
        this.type = flashcard.getType();
        this.queue = flashcard.getQueue();
        this.ivl = flashcard.getIvl();
        this.factor = flashcard.getFactor();
        this.reps = flashcard.getReps();
        this.lapses = flashcard.getLapses();
        this.left = flashcard.getLeft();
        this.due = flashcard.getDue();
    }
}
