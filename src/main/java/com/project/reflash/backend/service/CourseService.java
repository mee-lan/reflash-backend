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
import java.util.Set;
import java.util.stream.Collectors;

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

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course with the given id not found"));

        CourseEditDto courseEditDto = new CourseEditDto(course);
        return courseEditDto;
    }


    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Transactional
    public void resaveCourseWithNewData(CourseEditDto dto) {
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course with the given id not found"));

        // 1. Update basic fields
        course.setName(dto.getCourseName());
        course.setDescription(dto.getCourseDescription());
        course.setGrade(dto.getGrade());
        course.setAcademicYear(dto.getAcademicYear());

        syncTeachers(course, dto.getTeachers());
        syncStudents(course, dto.getStudents());

        // No need for repository.save(course) due to @Transactional dirty checking
    }

    private void syncTeachers(Course course, List<TeacherDto> dtoTeachers) {

        // 1. Collect IDs of teachers that SHOULD be in the course from the DTO
        Set<Integer> targetIds = dtoTeachers.stream()
                .map(TeacherDto::getId)
                .collect(Collectors.toSet());


        // 2. REMOVE: Remove teachers currently in the course who are NOT in the target ID set
        course.getTeachers().removeIf(t -> !targetIds.contains(t.getId()));


        // 3. ADD: Identify which IDs are actually NEW (not already in the course)
        Set<Integer> existingIds = course.getTeachers().stream()
                .map(Teacher::getId)
                .collect(Collectors.toSet());

        List<Integer> idsToAdd = targetIds.stream()
                .filter(id -> !existingIds.contains(id))
                .collect(Collectors.toList());

        if (!idsToAdd.isEmpty()) {
            List<Teacher> newTeachers = teacherRepository.findAllById(idsToAdd);


            // Check if the database found as many teachers as we requested
            if (newTeachers.size() != idsToAdd.size()) {

                // Find which IDs are missing for a better error message
                List<Integer> foundIds = newTeachers.stream().map(Teacher::getId).toList();
                idsToAdd.removeAll(foundIds);
                throw new RuntimeException("The following Teacher IDs do not exist: " + idsToAdd);
            }
            course.getTeachers().addAll(newTeachers);
        }
    }

    private void syncStudents(Course course, List<StudentDto> dtoStudents) {

        // 1. Collect IDs of students that SHOULD be in the course from the DTO
        Set<Integer> targetIds = dtoStudents.stream()
                .map(StudentDto::getId)
                .collect(Collectors.toSet());


        // 2. REMOVE: Remove students currently in the course who are NOT in the target ID set
        course.getStudents().removeIf(s -> !targetIds.contains(s.getId()));


        // 3. ADD: Identify which IDs are actually NEW (not already in the course)
        Set<Integer> existingIds = course.getStudents().stream()
                .map(Student::getId)
                .collect(Collectors.toSet());

        List<Integer> idsToAdd = targetIds.stream()
                .filter(id -> !existingIds.contains(id))
                .collect(Collectors.toList());

        if (!idsToAdd.isEmpty()) {
            List<Student> newStudents = studentRepository.findAllById(idsToAdd);
            if (newStudents.size() != idsToAdd.size()) {
                List<Integer> foundIds = newStudents.stream().map(Student::getId).toList();
                idsToAdd.removeAll(foundIds);
                throw new RuntimeException("The following Student IDs do not exist: " + idsToAdd);
            }
            course.getStudents().addAll(newStudents);
        }
    }

    @PreAuthorize("hasRole('TEACHER')")
    public List<StudentDto> getStudentsOfCourse(Integer courseId, Integer userId) {
        return courseRepository.getStudentsOfCourse(courseId, userId).stream().map(StudentDto::new).toList();
    }

    @PreAuthorize("hasRole('TEACHER')")
    public List<TeacherDto> getTeachersOfCourse(Integer courseId, Integer userId) {
        return courseRepository.getTeachersOfCourse(courseId, userId).stream().map(TeacherDto::new).toList();
    }
}
