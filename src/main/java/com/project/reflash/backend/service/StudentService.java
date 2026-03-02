package com.project.reflash.backend.service;

import com.project.reflash.backend.entity.Student;
import com.project.reflash.backend.exception.ExceptionMessage;
import com.project.reflash.backend.exception.UserDoesNotExistException;
import com.project.reflash.backend.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class StudentService {

    private final StudentRepository studentRepository;

    StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /**
     *
     * @param academicYearGradeSectionRoll
     * @return Enrollment
     * @throws UserDoesNotExistException if enrollment with year + grade + section + roll does not exist
     */
    public Student loadStudent(String academicYearGradeSectionRoll) {
        if(academicYearGradeSectionRoll == null || academicYearGradeSectionRoll.isBlank()) {
            throw new UserDoesNotExistException(ExceptionMessage.INVALID_USERNAME);
        }

        long count = academicYearGradeSectionRoll.chars().filter(c -> c == '_').count();

        if(count < 1) {
            throw new UserDoesNotExistException(ExceptionMessage.INVALID_USERNAME);
        }

        String year;
        String grade;
        String section;
        String roll;


        if(count == 2) {
            year = academicYearGradeSectionRoll.split("_")[0];
            grade = academicYearGradeSectionRoll.split("_")[1];
            section = "NONE";
            roll = academicYearGradeSectionRoll.split("_")[2];
        } else if(count == 3) {
            year = academicYearGradeSectionRoll.split("_")[0];
            grade = academicYearGradeSectionRoll.split("_")[1];
            section = academicYearGradeSectionRoll.split("_")[2];
            roll = academicYearGradeSectionRoll.split("_")[3];
        } else {
            throw new UserDoesNotExistException(ExceptionMessage.INVALID_USERNAME);
        }

        Optional<Student> student =  studentRepository.findByGradeSectionRollAndYear(year, grade, section, roll);

        if(student.isPresent()) {
            return student.get();
        } else {
            throw new UserDoesNotExistException(ExceptionMessage.USER_DOES_NOT_EXIST);
        }
    }
}
