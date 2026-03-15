package com.project.reflash.backend.service;

import com.project.reflash.backend.dto.CourseStudentDto;
import com.project.reflash.backend.repository.CourseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CourseService {
    private final CourseRepository courseRepository;

    CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<CourseStudentDto> getCoursesOfStudent(Integer studentId) {
        //TODO: implement this properly(ie retrieve it properly)
        return this.courseRepository.getCoursesOfStudent(studentId).stream().map(course -> {
                    Integer deckCount = course.getDecks() != null ? course.getDecks().size() : 0;
                    List<String> teacherNames;
                    if(course.getTeachers() != null) {
                        teacherNames = course.getTeachers().stream()
                                .map(teacher -> teacher.getFirstName() + " " + teacher.getLastName()).toList();
                    } else {
                        teacherNames = new ArrayList<>();
                    }
                    return new CourseStudentDto(course, deckCount, teacherNames);
                }
        ).toList();
    }

    public List<CourseStudentDto> getCoursesOfTeacher(Integer teacherId) {
        //TODO: implement this properly
        return this.courseRepository.getCoursesOfTeacher(teacherId).stream().map(CourseStudentDto::new).toList();
    }
}
