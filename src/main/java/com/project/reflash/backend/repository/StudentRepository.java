package com.project.reflash.backend.repository;

import com.project.reflash.backend.entity.Enrollment;
import com.project.reflash.backend.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
        @Query("SELECT e FROM Enrollment e JOIN FETCH e.student s " +
                "WHERE e.grade = :grade AND e.section = :section AND e.roll = :roll AND e.academicYear = :year")
        Optional<Enrollment> findByGradeSectionRollAndYear(
                @Param("year") String year,
                @Param("grade") String grade,
                @Param("section") String section,
                @Param("roll") String roll);
}
