package com.project.reflash.backend.controller;


import com.project.reflash.backend.auth.user_details.StudentUserDetails;
import com.project.reflash.backend.auth.user_details.TeacherUserDetails;
import com.project.reflash.backend.dto.*;
import com.project.reflash.backend.response.ApiResponse;
import com.project.reflash.backend.service.CourseService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api")
public class CourseController {

    CourseService courseService;

    CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/student/courses")
    public ResponseEntity<ApiResponse> getCoursesOfStudent(@AuthenticationPrincipal StudentUserDetails student) {
        List<CourseStudentDto> courses = courseService.getCoursesOfStudent(student.getId());
        return new ResponseEntity<ApiResponse>(new ApiResponse(courses), HttpStatus.OK);
    }


    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PostMapping("/admin/course")
    public ResponseEntity<ApiResponse> getCoursesOfStudent(@RequestBody CourseCreationDto courseCreationDto) {
        courseService.createCourse(courseCreationDto);
        return new ResponseEntity<ApiResponse>(new ApiResponse("Course Created Successfully"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/teacher/courses")
    public ResponseEntity<ApiResponse> getCoursesOfStudent(HttpSession session, @AuthenticationPrincipal TeacherUserDetails teacher) {
        List<CourseTeacherDto> courses = courseService.getCoursesOfTeacher(teacher.getId());
        return new ResponseEntity<ApiResponse>(new ApiResponse(courses), HttpStatus.OK);
    }



    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @GetMapping("/admin/course-full")
    public ResponseEntity<ApiResponse> getCourseForEdit(@RequestParam Integer courseId) {
        CourseEditDto courseEditDto = courseService.getCourseForEdit(courseId);
        return ResponseEntity.ok(new ApiResponse(courseEditDto));
    }


    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @GetMapping("/admin/all-course")
    public ResponseEntity<ApiResponse> getAllCourses() {
        List<CourseAdministratorDto> courses = courseService.getAllCourses();
        return ResponseEntity.ok(new ApiResponse(courses));
    }


    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PutMapping("/admin/edit-course")
    public ResponseEntity<ApiResponse> resaveCourseWithNewData(@RequestBody CourseEditDto courseEditDto) {
        courseService.resaveCourseWithNewData(courseEditDto);
        return ResponseEntity.ok(new ApiResponse("Edit Successful"));
    }
}
