package com.project.reflash.backend.repository;


import com.project.reflash.backend.entity.Course;
import com.project.reflash.backend.entity.Deck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeckRepository extends JpaRepository<Deck, Integer> {

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
}
