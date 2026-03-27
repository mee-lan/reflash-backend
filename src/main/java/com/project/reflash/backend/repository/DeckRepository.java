package com.project.reflash.backend.repository;


import com.project.reflash.backend.entity.Deck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeckRepository extends JpaRepository<Deck, Integer>, JpaSpecificationExecutor<Deck> {

    @Query("""
       SELECT d
       FROM Deck d
       JOIN d.course c
       JOIN c.students s
       WHERE c.id = :courseId
       AND s.id = :studentId
       """)
    List<Deck> getDecksOfStudent(@Param("studentId") Integer studentId,
                                   @Param("courseId") Integer courseId);


    @Query("""
       SELECT d
       FROM Deck d
       JOIN d.course c
       JOIN c.teachers t
       WHERE c.id = :courseId
       AND t.id = :teacherId
       """)
    List<Deck> getDecksOfTeacher(@Param("teacherId") Integer teacherId,
                                   @Param("courseId") Integer courseId);



    /*

    RAW SQL QUERY: to access a deck by its id only if the student.id has access to it

    select decks.* from decks, courses, course_student, students where decks.id = 1 AND courses.id = decks.course_id AND courses.id = course_student.course_id AND course_student.student_id = students.id AND students.id = 1;

    ----------------------------------------------------------------------------

    SIMPLIFIES SQL QUERY: to access a deck by its id only if the student.id has access to it

    select decks.* from decks join courses ON decks.course_id = courses.id join course_student ON courses.id = course_student.course_id join students ON course_student.student_id = students.id where decks.id = 1 and students.id = 1;
     */


    @Query("""
        SELECT d
        FROM Deck d
        join d.course c
        join c.students cs
        WHERE d.id = :deckId
        AND cs.id = :userId
    """)
    Optional<Deck> getDeckByIdIfAccessibleByStudent(@Param("deckId") Integer deckId, @Param("userId") Integer userId);


    @Query("""
        SELECT d
        FROM Deck d
        join d.course c
        join c.teachers t
        WHERE d.id = :deckId
        AND t.id = :userId
    """)
    Optional<Deck> getDeckByIdIfAccessibleByTeacher(@Param("deckId") Integer deckId, @Param("userId") Integer userId);
}
