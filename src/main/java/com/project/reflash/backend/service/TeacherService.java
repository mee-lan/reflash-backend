package com.project.reflash.backend.service;

import com.project.reflash.backend.dto.StudentCreationDto;
import com.project.reflash.backend.dto.TeacherCreationDto;
import com.project.reflash.backend.dto.TeacherDto;
import com.project.reflash.backend.entity.Student;
import com.project.reflash.backend.entity.Teacher;
import com.project.reflash.backend.exception.ExceptionMessage;
import com.project.reflash.backend.exception.UserDoesNotExistException;
import com.project.reflash.backend.repository.TeacherRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;

    TeacherService(TeacherRepository teacherRepository, PasswordEncoder passwordEncoder) {
        this.teacherRepository = teacherRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<TeacherDto> getAllTeachers() {
        List<Teacher> teachers = teacherRepository.getAllTeacher();
        return teachers.stream().map(TeacherDto::new).toList();
    }


    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public void createTeacherProfile(TeacherCreationDto teacherCreationDto) {
        //TODO: validate the Student Creation Dto
        Teacher teacher = new Teacher();


        //make sure that a teacher with the same academicYear, grade, roll number, section exists at the same time
        Optional<Teacher> existingTeacher = teacherRepository.findByUsername(
                teacherCreationDto.getUsername());

        if (existingTeacher.isPresent()) {
            throw new RuntimeException("The teacher with this username is already present");
        }

        teacher.setFirstName(teacherCreationDto.getFirstName());
        teacher.setLastName(teacherCreationDto.getLastName());
        String encodedPassword = passwordEncoder.encode(teacherCreationDto.getPassword());
        teacher.setPassword(encodedPassword);
        teacher.setEmail(teacherCreationDto.getEmail());
        teacher.setUsername(teacherCreationDto.getUsername());

        teacherRepository.save(teacher);
    }


}
