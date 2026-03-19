package com.project.reflash.backend.service.security;

import com.project.reflash.backend.auth.user_details.AdministratorUserDetails;
import com.project.reflash.backend.auth.user_details.StudentUserDetails;
import com.project.reflash.backend.auth.user_details.TeacherUserDetails;
import com.project.reflash.backend.dto.Administrator;
import com.project.reflash.backend.entity.Student;
import com.project.reflash.backend.entity.Teacher;
import com.project.reflash.backend.exception.InvalidRoleException;
import com.project.reflash.backend.service.AdministratorService;
import com.project.reflash.backend.service.StudentService;
import com.project.reflash.backend.service.TeacherService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
public class DatabaseUserDetailsService implements UserDetailsService {
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final AdministratorService administratorService;
    private UserDetails userDetails;

    public DatabaseUserDetailsService(StudentService studentService, TeacherService teacherService, AdministratorService administratorService) {
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.administratorService = administratorService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String role = request.getHeader("role");
        log.info("Loading user {} with role {}", username, role);

        if (role.trim().equalsIgnoreCase("STUDENT")) {
            Student student = studentService.loadStudent(username);
            userDetails = new StudentUserDetails(student.getId(), student.getFirstName(), student.getLastName(),
                    student.getAcademicYear(), student.getGrade(), student.getSection(),
                    student.getRoll(), student.getPassword());
        } else if (role.trim().equalsIgnoreCase("TEACHER")) {
            Teacher teacher = teacherService.loadTeacher(username);
            userDetails = new TeacherUserDetails(teacher.getId(), teacher.getFirstName(), teacher.getLastName(),
                    teacher.getUsername(), teacher.getPassword(), teacher.getEmail());
        } else if (role.trim().equalsIgnoreCase("ADMINISTRATOR")) {
            Administrator administrator = administratorService.loadAdministrator(username);
            userDetails = new AdministratorUserDetails(administrator.getId(), administrator.getFirstName(),
                    administrator.getLastName(),
                    administrator.getUsername(), administrator.getPassword(), administrator.getEmail());
        } else {
            throw new InvalidRoleException();
        }
        return userDetails;
    }
}
