package com.project.reflash.backend.service;

import com.project.reflash.backend.dto.StudentCreationDto;
import com.project.reflash.backend.dto.StudentDto;
import com.project.reflash.backend.entity.Student;
import com.project.reflash.backend.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentService(com.project.reflash.backend.repository.StudentRepository studentRepository, PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public List<StudentDto> getAllStudentsByGrade(String grade) {
        List<Student> students = studentRepository.getAllStudentsByGrade(grade);
        return students.stream().map(StudentDto::new).toList();
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public void createStudentProfile(StudentCreationDto studentCreationDto) {
        //TODO: validate the Student Creation Dto
        Student student = new Student();


        //make sure that a student with the same academicYear, grade, roll number, section exists at the same time
        Optional<Student> existingStudent = studentRepository.findByYearGradeSectionAndRoll(
                studentCreationDto.getAcademicYear(), studentCreationDto.getGrade(), studentCreationDto.getSection(),
                studentCreationDto.getRoll());

        if (existingStudent.isPresent()) {
            throw new RuntimeException("The student with the academic year, grade, section, roll already exists");
        }

        student.setFirstName(studentCreationDto.getFirstName());
        student.setLastName(studentCreationDto.getLastName());
        student.setGrade(studentCreationDto.getGrade());
        String encodedPassword = passwordEncoder.encode(studentCreationDto.getPassword());
        student.setPassword(encodedPassword);
        student.setAcademicYear(studentCreationDto.getAcademicYear());
        student.setRoll(studentCreationDto.getRoll());

        //NOTE: section is automatically set to 'NONE' during Student object creation
        if (studentCreationDto.getSection() != null && !studentCreationDto.getSection().isBlank()) {
            student.setSection(studentCreationDto.getSection());
        }

        studentRepository.save(student);
    }
}
