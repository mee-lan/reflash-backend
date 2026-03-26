package com.project.reflash.backend.repository;

import com.project.reflash.backend.entity.Course;
import com.project.reflash.backend.entity.Student;
import com.project.reflash.backend.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    @Query("SELECT c FROM Course c JOIN c.students s WHERE s.id = :studentId")
    public List<Course> getCoursesOfStudent(@Param("studentId") Integer studentId);

    @Query("SELECT c FROM Course c JOIN c.teachers t WHERE t.id = :teacherId")
    public List<Course> getCoursesOfTeacher(@Param("teacherId") Integer teacherId);

    @Query("SELECT s FROM Course c JOIN c.students s JOIN c.teachers t WHERE c.id = :courseId AND t.id = :teacherId")
    public List<Student> getStudentsOfCourse(@Param("courseId") Integer courseId,
                                             @Param("teacherId") Integer teacherId);

    @Query("SELECT t from Course c JOIN c.teachers t WHERE c.id = :courseId AND t.id = :teacherId")
    public List<Teacher> getTeachersOfCourse(@Param("courseId") Integer courseId, @Param("teacherId") Integer teacherId);
}
