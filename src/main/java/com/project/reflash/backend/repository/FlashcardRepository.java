package com.project.reflash.backend.repository;

import com.project.reflash.backend.entity.Flashcard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlashcardRepository extends JpaRepository<Flashcard, Integer> {

    // ── Student-scoped queries ──────────────────────────────────────────────

//    @Query("""
//            SELECT f
//            FROM Flashcard f
//            JOIN f.deck d
//            JOIN d.course c
//            JOIN c.students s
//            WHERE f.type = 'NEW' AND d.id = :deckId AND s.id = :userId
//            ORDER BY f.crt
//            """)
//    List<Flashcard> getNewCardsForStudent(@Param("deckId") Integer deckId,
//                                          @Param("userId") Integer userId,
//                                          Pageable pageable);
//
//    @Query("""
//            SELECT f
//            FROM Flashcard f
//            JOIN f.deck d
//            JOIN d.course c
//            JOIN c.students s
//            WHERE (f.type = 'LEARNING' OR f.type = 'RELEARNING')
//            AND f.due < :currentTime AND d.id = :deckId AND s.id = :userId
//            """)
//    List<Flashcard> getLearningRelearningCardsForStudent(@Param("deckId") Integer deckId,
//                                                         @Param("currentTime") Long currentTime,
//                                                         @Param("userId") Integer userId);
//
//    @Query("""
//            SELECT f
//            FROM Flashcard f
//            JOIN f.deck d
//            JOIN d.course c
//            JOIN c.students s
//            WHERE f.type = 'REVIEW'
//            AND f.due < :today AND d.id = :deckId AND s.id = :userId
//            """)
//    List<Flashcard> getReviewCardsForStudent(@Param("deckId") Integer deckId,
//                                             @Param("today") Long today,
//                                             @Param("userId") Integer userId);
//
//    // ── Teacher-scoped queries ──────────────────────────────────────────────
//
//    @Query("""
//            SELECT f
//            FROM Flashcard f
//            JOIN f.deck d
//            JOIN d.course c
//            JOIN c.teachers t
//            WHERE f.type = 'NEW' AND d.id = :deckId AND t.id = :userId
//            ORDER BY f.crt
//            """)
//    List<Flashcard> getNewCardsForTeacher(@Param("deckId") Integer deckId,
//                                          @Param("userId") Integer userId,
//                                          Pageable pageable);
//
//    @Query("""
//            SELECT f
//            FROM Flashcard f
//            JOIN f.deck d
//            JOIN d.course c
//            JOIN c.teachers t
//            WHERE (f.type = 'LEARNING' OR f.type = 'RELEARNING')
//            AND f.due < :currentTime AND d.id = :deckId AND t.id = :userId
//            """)
//    List<Flashcard> getLearningRelearningCardsForTeacher(@Param("deckId") Integer deckId,
//                                                         @Param("currentTime") Long currentTime,
//                                                         @Param("userId") Integer userId);
//
//    @Query("""
//            SELECT f
//            FROM Flashcard f
//            JOIN f.deck d
//            JOIN d.course c
//            JOIN c.teachers t
//            WHERE f.type = 'REVIEW'
//            AND f.due < :today AND d.id = :deckId AND t.id = :userId
//            """)
//    List<Flashcard> getReviewCardsForTeacher(@Param("deckId") Integer deckId,
//                                             @Param("today") Long today,
//                                             @Param("userId") Integer userId);
}
