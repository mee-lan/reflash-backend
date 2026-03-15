package com.project.reflash.backend.dto;

import com.project.reflash.backend.entity.Course;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CourseStudentDto {
    private Integer id;
    private String name;
    private String description;
    private Integer deckCount;
    private List<String> teacherNames;

    public CourseStudentDto(Course course) {
        this.id = course.getId();
        this.name = course.getCourseName();
        this.description = course.getDescription();
    }

    public CourseStudentDto(Course course, Integer deckCount, List<String> teacherNames) {
        this(course);
        this.deckCount = deckCount;
        this.teacherNames = teacherNames;
    }
}
