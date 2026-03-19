package com.project.reflash.backend.controller;

import com.project.reflash.backend.auth.user_details.StudentUserDetails;
import com.project.reflash.backend.dto.CourseStudentDto;
import com.project.reflash.backend.dto.StudentCreationDto;
import com.project.reflash.backend.dto.TeacherCreationDto;
import com.project.reflash.backend.dto.TeacherDto;
import com.project.reflash.backend.response.ApiResponse;
import com.project.reflash.backend.service.TeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api")
public class TeacherController {
    private final TeacherService teacherService;

    TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @GetMapping("/admin/teachers")
    public ResponseEntity<ApiResponse> getAllTeachers() {
        List<TeacherDto> teachers = teacherService.getAllTeachers();
        return new ResponseEntity<ApiResponse>(new ApiResponse(teachers), HttpStatus.OK);
    }


    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PostMapping("/admin/teacher-profile")
    public ResponseEntity<ApiResponse> createTeacherProfile(@RequestBody TeacherCreationDto teacherCreationDto) {
        teacherService.createTeacherProfile(teacherCreationDto);
        return new ResponseEntity<ApiResponse>(new ApiResponse("Teacher profile created successfully"), HttpStatus.OK);
    }
}
