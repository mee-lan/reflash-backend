package com.project.reflash.backend.repository;

import com.project.reflash.backend.entity.Flashcard;
import com.project.reflash.backend.entity.Note;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlashcardRepository extends JpaRepository<Flashcard, Integer> {

    // ── Student-scoped queries ──────────────────────────────────────────────

    /*
    sql query:

    select notes.* from notes join decks ON notes.deck_id = decks.id left join flashcards ON flashcards.note_id = notes.id where decks.id = 1 AND flashcards.id is
     null;

     */
    @Query("""
                SELECT n
                FROM Note n
                WHERE n.flashCards IS EMPTY
                  AND n.deck.id = :deckId
            """)
    List<Note> getNewCardsForStudent(@Param("deckId") Integer deckId,
                                     Pageable pageable);

    /*

    sql query:

    select flashcards.*, notes.* from flashcards join notes ON flashcards.note_id = notes.id join decks ON notes.deck_id = decks.id WHERE decks.id = 1 AND (flashcards.type = 'LEARNING' OR flashcards.type = 'RELEARNING');

     */
    @Query("""
         select f
          from Flashcard f
            join f.note n
                join n.deck d
                    WHERE d.id = :deckId
                        AND (f.type = 'LEARNING' OR f.type='RELEARNING')
                            AND f.due < :currentTimeSeconds
    """)
    List<Flashcard> getLearningRelearingCardsForStudent(@Param("deckId") Integer deckId, @Param("currentTimeSeconds") Long currentTimeSeconds);


    /*

    sql query:

    select flashcards.*, notes.* from flashcards join notes ON flashcards.note_id = notes.id join decks ON notes.deck_id = decks.id WHERE decks.id = 1 AND flashcards.type = 'REVIEW';

     */
    @Query("""
         select f
          from Flashcard f
            join f.note n
                join n.deck d
                    WHERE d.id = :deckId
                        AND f.type = 'REVIEW'
                            AND f.due < :today
    """)
    List<Flashcard> getReviewCardsForStudent(@Param("deckId") Integer deckId, @Param("today") Long today);
}
