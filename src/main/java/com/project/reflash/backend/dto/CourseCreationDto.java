package com.project.reflash.backend.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class CourseCreationDto {
    String courseName;
    String courseDescription;
    String grade;
    String academicYear;
    List<Integer> teachers;
    List<Integer> students;
}
