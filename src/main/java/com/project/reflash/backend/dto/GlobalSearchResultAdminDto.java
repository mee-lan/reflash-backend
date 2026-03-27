package com.project.reflash.backend.dto;

import com.project.reflash.backend.entity.Course;
import com.project.reflash.backend.entity.Student;
import com.project.reflash.backend.entity.Teacher;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class GlobalSearchResultAdminDto {
    List<CourseStudentDto> courses = new ArrayList<>();
    List<TeacherDto> teachers = new ArrayList<>();
    List<StudentDto> students = new ArrayList<>();

    public GlobalSearchResultAdminDto(List<Course> courses, List<Teacher> teachers, List<Student> students) {
        this.courses = courses.stream().map(CourseStudentDto::new).toList();
        this.teachers = teachers.stream().map(TeacherDto::new).toList();
        this.students = students.stream().map(StudentDto::new).toList();
    }
}
