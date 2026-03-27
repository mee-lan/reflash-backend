package com.project.reflash.backend.dto;

import com.project.reflash.backend.algorithm.CardQueue;
import com.project.reflash.backend.algorithm.CardType;
import com.project.reflash.backend.entity.Flashcard;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

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
    private String dueFormatted;
    private boolean dirty = false;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.of("Asia/Kathmandu"));

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd")
            .withZone(ZoneId.of("Asia/Kathmandu"));

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

    public FlashcardDto(Flashcard flashcard, long deckCrt) {
        this(flashcard);
        this.dueFormatted = calculateDueFormatted(flashcard, deckCrt);
    }

    private String calculateDueFormatted(Flashcard flashcard, long deckCrt) {
        return switch (flashcard.getType()) {
            case NEW -> "0";
            case LEARNING, RELEARNING -> DATE_TIME_FORMATTER.format(Instant.ofEpochSecond(flashcard.getDue()));
            case REVIEW -> {
                long dueEpochSeconds = deckCrt + (flashcard.getDue() * 86400L);
                yield DATE_FORMATTER.format(Instant.ofEpochSecond(dueEpochSeconds));
            }
        };
    }
}
