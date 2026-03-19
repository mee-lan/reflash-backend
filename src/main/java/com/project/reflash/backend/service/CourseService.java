package com.project.reflash.backend.service;

import com.project.reflash.backend.dto.*;
import com.project.reflash.backend.entity.Course;
import com.project.reflash.backend.entity.Student;
import com.project.reflash.backend.entity.Teacher;
import com.project.reflash.backend.repository.CourseRepository;
import com.project.reflash.backend.repository.StudentRepository;
import com.project.reflash.backend.repository.TeacherRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    CourseService(CourseRepository courseRepository, TeacherRepository teacherRepository, StudentRepository studentRepository) {
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
    }

    public List<CourseStudentDto> getCoursesOfStudent(Integer studentId) {
        //TODO: implement this properly(ie retrieve it properly)
        return this.courseRepository.getCoursesOfStudent(studentId).stream().map(course -> {
                    Integer deckCount = course.getDecks() != null ? course.getDecks().size() : 0;
                    List<String> teacherNames;
                    if (course.getTeachers() != null) {
                        teacherNames = course.getTeachers().stream()
                                .map(teacher -> teacher.getFirstName() + " " + teacher.getLastName()).toList();
                    } else {
                        teacherNames = new ArrayList<>();
                    }
                    return new CourseStudentDto(course, deckCount, teacherNames);
                }
        ).toList();
    }

    public List<CourseTeacherDto> getCoursesOfTeacher(Integer teacherId) {
        //TODO: implement this properly
        return this.courseRepository.getCoursesOfTeacher(teacherId).stream().map(course -> {
                    Integer deckCount = course.getDecks() != null ? course.getDecks().size() : 0;

                    Integer studentCount = course.getStudents() != null ? course.getStudents().size() : 0;
                    return new CourseTeacherDto(course, deckCount, studentCount);
                }
        ).toList();
    }

    @Transactional
    public void createCourse(CourseCreationDto courseCreationDto) {
        //TODO: validate that the courseCreationDto is valid, there must be students and teachers, courseName, description, and other fields

        List<Teacher> teachers = teacherRepository.findAllById(courseCreationDto.getTeachers());

        if (teachers.size() != courseCreationDto.getTeachers().size()) {
            throw new RuntimeException("Some teacher IDs are invalid");
        }

        List<Student> students = studentRepository.findAllById(courseCreationDto.getStudents());


        //TODO: also validate that the students are in the same grade
        if (students.size() != courseCreationDto.getStudents().size()) {
            throw new RuntimeException("Some students IDs are invalid");
        }

        Course course = new Course();
        course.setName(courseCreationDto.getCourseName());
        course.setDescription(courseCreationDto.getCourseDescription());
        course.setGrade(courseCreationDto.getGrade());
        course.setAcademicYear(courseCreationDto.getAcademicYear());
        course.setTeachers(teachers);
        course.setStudents(students);

        courseRepository.save(course);
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public CourseEditDto getCourseForEdit(Integer courseId) {

        Course course = courseRepository.findById(courseId).orElseThrow(()-> new RuntimeException("Course with the given id not found"));

        CourseEditDto courseEditDto = new CourseEditDto(course);
        return courseEditDto;
    }

    @Transactional
    public void resaveCourseWithNewData(CourseCreationDto dto) {


    }
}
