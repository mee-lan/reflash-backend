package com.project.reflash.backend.service;

import com.project.reflash.backend.dto.StudentDto;
import com.project.reflash.backend.entity.Student;
import com.project.reflash.backend.exception.ExceptionMessage;
import com.project.reflash.backend.exception.UserDoesNotExistException;
import com.project.reflash.backend.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class StudentService {

    private final StudentRepository studentRepository;

    StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public List<StudentDto> getAllStudentsByGrade(String grade) {
        List<Student> students = studentRepository.getAllStudentsByGrade(grade);
        return students.stream().map(StudentDto::new).toList();
    }
}
