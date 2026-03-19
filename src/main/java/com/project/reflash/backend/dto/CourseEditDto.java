package com.project.reflash.backend.dto;

import com.project.reflash.backend.entity.Course;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class CourseEditDto {
    private Integer courseId;
    private String courseName;
    private String courseDescription;
    private String grade;
    private String academicYear;
    private List<TeacherDto> teachers;
    private List<StudentDto> students;

    public CourseEditDto(Course course ) {
        this.courseId = course.getId();
        this.courseName = course.getName();
        this.courseDescription = course.getDescription();
        this.grade = course.getGrade();
        this.academicYear = course.getAcademicYear();
        this.teachers = course.getTeachers().stream().map(TeacherDto::new).toList();
        this.students = course.getStudents().stream().map(StudentDto::new).toList();
    }
}
