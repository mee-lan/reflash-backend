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
    NOTE: we need to get all the notes from a particular deck that don't have a flashcard for a particular stuent

     updated query:

        SELECT n.*
        FROM notes n
        JOIN decks d
          ON d.id = n.deck_id
        WHERE d.id = 2
          AND NOT EXISTS (
            SELECT 1
            FROM flashcards f
            WHERE f.note_id = n.id
              AND f.student_id = 1
          );

     */
    @Query("""
                SELECT n
                FROM Note n
                JOIN n.deck d
                WHERE d.id = :deckId
                  AND NOT EXISTS (
                      SELECT 1
                      FROM Flashcard f
                      WHERE f.note = n
                        AND f.student.id = :studentId
                  )
            """)
    List<Note> getOrphanNotes(@Param("deckId") Integer deckId,
                              @Param("studentId") Integer studentId,
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
                                    join f.student s
                            WHERE d.id = :deckId
                                AND (f.type = 'LEARNING' OR f.type='RELEARNING')
                                                AND s.id = :studentId
            """)
    List<Flashcard> getLearningRelearingCardsForStudent(@Param("deckId") Integer deckId,  @Param("studentId") Integer studentId);

    @Query("""
                 select f
                  from Flashcard f
                    join f.note n
                        join n.deck d
                                    join f.student s
                            WHERE d.id = :deckId
                                AND f.type = 'NEW'
                                            AND s.id = :studentId
            """)
    List<Flashcard> getNewCardsForStudent(@Param("deckId") Integer deckId, @Param("studentId") Integer studentId);


    /*

    sql query:

    select flashcards.*, notes.* from flashcards join notes ON flashcards.note_id = notes.id join decks ON notes.deck_id = decks.id WHERE decks.id = 1 AND flashcards.type = 'REVIEW';

     */
    @Query("""
                 select f
                  from Flashcard f
                    join f.note n
                        join n.deck d
                                    join f.student s
                            WHERE d.id = :deckId
                                AND f.type = 'REVIEW'
                                    AND f.due < :today
                                                AND s.id = :studentId
            """)
    List<Flashcard> getReviewCardsForStudentDueDateExpired(@Param("deckId") Integer deckId, @Param("today") Long today, @Param("studentId") Integer studentId);


    @Query("""
                 select f
                  from Flashcard f
                    join f.note n
                        join n.deck d
                                    join f.student s
                            WHERE d.id = :deckId
                                AND f.type = 'REVIEW'
                                                AND s.id = :studentId
            """)
    List<Flashcard> getReviewCardsForStudent(@Param("deckId") Integer deckId,@Param("studentId") Integer studentId);


    @Query("""
                select f
                from Flashcard f
                    join f.note n
                    join n.deck d
                WHERE d.id = :deckId
                  AND f.id in :flashcardIds
                  AND f.student.id = :studentId
            """)
    List<Flashcard> getCardsByIdsOfADeck(@Param("flashcardIds") List<Integer> flashcardIds, @Param("deckId") Integer deckId, @Param("studentId") Integer studentId);
}
