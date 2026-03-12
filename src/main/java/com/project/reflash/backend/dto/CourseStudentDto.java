package com.project.reflash.backend.dto;

import com.project.reflash.backend.entity.Course;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseStudentDto {
    private Integer id;
    private String name;

    public CourseStudentDto(Course course) {
        this.id = course.getId();
        this.name = course.getCourseName();
    }
}
