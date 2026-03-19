package com.project.reflash.backend.dto;

import com.project.reflash.backend.algorithm.CardQueue;
import com.project.reflash.backend.algorithm.CardType;
import com.project.reflash.backend.entity.Flashcard;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FlashcardDto {
    private Integer id;
    private NoteDto note;
    private CardType type;
    private CardQueue queue;
    private Integer ivl;
    private Integer factor;
    private Integer reps;
    private Integer lapses;
    private Integer left;
    private Long due;
    private boolean dirty = false;

    public FlashcardDto(Flashcard flashcard) {
        this.id = flashcard.getId();
        this.note = new NoteDto(flashcard.getNote());
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
