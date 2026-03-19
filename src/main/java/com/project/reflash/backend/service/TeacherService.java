package com.project.reflash.backend.service;

import com.project.reflash.backend.dto.TeacherDto;
import com.project.reflash.backend.entity.Teacher;
import com.project.reflash.backend.exception.ExceptionMessage;
import com.project.reflash.backend.exception.UserDoesNotExistException;
import com.project.reflash.backend.repository.TeacherRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TeacherService {
    private final TeacherRepository teacherRepository;

    TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public List<TeacherDto> getAllTeachers() {
        List<Teacher> teachers = teacherRepository.getAllTeacher();
        return teachers.stream().map(TeacherDto::new).toList();
    }
}
