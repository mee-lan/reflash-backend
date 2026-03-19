package com.project.reflash.backend.controller;

import com.project.reflash.backend.dto.StudentCreationDto;
import com.project.reflash.backend.dto.StudentDto;
import com.project.reflash.backend.response.ApiResponse;
import com.project.reflash.backend.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api")
public class StudentController {
    private final StudentService studentService;
    StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @GetMapping("/admin/students-by-grade")
    public ResponseEntity<ApiResponse> getAllStudentsByGrade(@RequestParam("grade") String grade) {
        List<StudentDto> students = studentService.getAllStudentsByGrade(grade);
        return new ResponseEntity<ApiResponse>(new ApiResponse(students), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PostMapping("/admin/student-profile")
    public ResponseEntity<ApiResponse> createStudentProfile(@RequestBody StudentCreationDto studentCreationDto) {
        studentService.createStudentProfile(studentCreationDto);
        return new ResponseEntity<ApiResponse>(new ApiResponse("Student profile created successfully"), HttpStatus.OK);
    }
}
