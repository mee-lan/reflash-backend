package com.project.reflash.backend.dto;

import com.project.reflash.backend.entity.Course;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseTeacherDto {
    private Integer couresId;
    private String courseName;
    private String courseDescription;
    private Integer deckCount;
    private Integer studentCount;
    private String grade;

    public CourseTeacherDto(Course course) {
        this.couresId = course.getId();
        this.courseName = course.getCourseName();
        this.grade = course.getGrade();
        this.courseDescription = course.getDescription();
    }

    public CourseTeacherDto(Course course, Integer deckCount, Integer studentCount) {
        this(course);
        this.deckCount = deckCount;
        this.studentCount = studentCount;
    }
}
