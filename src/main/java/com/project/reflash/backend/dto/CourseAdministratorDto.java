package com.project.reflash.backend.dto;

import com.project.reflash.backend.entity.Course;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseAdministratorDto {
    private Integer couresId;
    private String courseName;
    private String courseDescription;
    private String grade;

    public CourseAdministratorDto(Course course) {
        this.couresId = course.getId();
        this.courseName = course.getName();
        this.grade = course.getGrade();
        this.courseDescription = course.getDescription();
    }
}
