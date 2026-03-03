package com.project.reflash.backend.dto;

import com.project.reflash.backend.entity.Course;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseTeacherDto {
    private Integer id;
    private String courseName;
    private String grade;

    public CourseTeacherDto(Course course) {
        this.id = course.getId();
        this.courseName = course.getCourseName();
        this.grade = course.getGrade();
    }
}
