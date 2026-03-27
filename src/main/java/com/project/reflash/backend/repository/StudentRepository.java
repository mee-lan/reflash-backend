package com.project.reflash.backend.repository;

import com.project.reflash.backend.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface StudentRepository extends JpaRepository<Student, Integer>, JpaSpecificationExecutor<Student> {
        @Query("SELECT s FROM Student s " +
                "WHERE s.grade = :grade AND s.section = :section AND s.roll = :roll AND s.academicYear = :year")
        Optional<Student> findByYearGradeSectionAndRoll(
                @Param("year") String year,
                @Param("grade") String grade,
                @Param("section") String section,
                @Param("roll") String roll);

        @Query("Select s from Student s where s.grade= :grade")
        List<Student> getAllStudentsByGrade(@Param("grade") String grade);

}
