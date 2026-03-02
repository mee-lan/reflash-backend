package com.project.reflash.backend.service.security;

import com.project.reflash.backend.entity.Enrollment;
import com.project.reflash.backend.entity.Student;
import com.project.reflash.backend.exception.ExceptionMessage;
import com.project.reflash.backend.exception.UserDoesNotExistException;
import com.project.reflash.backend.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Slf4j
public class DatabaseUserDetailsService implements UserDetailsService {
    StudentService studentService;

    public DatabaseUserDetailsService(StudentService studentService) {
        this.studentService = studentService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Enrollment enrollment = studentService.loadEnrollment(username);
            log.info("UserDetailsService invoked by: {}", username);

            if (enrollment != null) {
                Student student = enrollment.getStudent();
                UserDetails user = new StudentUserDetails(student.getId(), student.getFirstName(), student.getLastName(), enrollment.getAcademicYear() , enrollment.getGrade(), enrollment.getSection(), enrollment.getRoll(), student.getPassword(), "ROLE_STUDENT");
                return user;
            } else {
                throw new UserDoesNotExistException(ExceptionMessage.USER_DOES_NOT_EXIST);
            }
        } catch (UserDoesNotExistException e) {
            throw new UsernameNotFoundException("@ " + username + "username not found");
        }
    }
}
