package com.project.reflash.backend.service.security;

import com.project.reflash.backend.auth.user_details.AdministratorUserDetails;
import com.project.reflash.backend.auth.user_details.StudentUserDetails;
import com.project.reflash.backend.auth.user_details.TeacherUserDetails;
import com.project.reflash.backend.dto.Administrator;
import com.project.reflash.backend.entity.Student;
import com.project.reflash.backend.entity.Teacher;
import com.project.reflash.backend.exception.ExceptionMessage;
import com.project.reflash.backend.exception.InvalidRoleException;
import com.project.reflash.backend.exception.UserDoesNotExistException;
import com.project.reflash.backend.repository.AdministratorRepository;
import com.project.reflash.backend.repository.StudentRepository;
import com.project.reflash.backend.repository.TeacherRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

@Slf4j
public class DatabaseUserDetailsService implements UserDetailsService {
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final AdministratorRepository administratorRepository;
    private UserDetails userDetails;

    public DatabaseUserDetailsService(StudentRepository studentRepository, TeacherRepository teacherRepository, AdministratorRepository administratorRepository) {
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.administratorRepository = administratorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String role = request.getHeader("role");
        log.info("Loading user {} with role {}", username, role);

        if (role.trim().equalsIgnoreCase("STUDENT")) {
            Student student = loadStudentForAuthentication(username);
            userDetails = new StudentUserDetails(student.getId(), student.getFirstName(), student.getLastName(),
                    student.getAcademicYear(), student.getGrade(), student.getSection(),
                    student.getRoll(), student.getPassword());
        } else if (role.trim().equalsIgnoreCase("TEACHER")) {
            Teacher teacher = loadTeacherForAuthentication(username);
            userDetails = new TeacherUserDetails(teacher.getId(), teacher.getFirstName(), teacher.getLastName(),
                    teacher.getUsername(), teacher.getPassword(), teacher.getEmail());
        } else if (role.trim().equalsIgnoreCase("ADMINISTRATOR")) {
            Administrator administrator = loadAdministratorForAuthentication(username);
            userDetails = new AdministratorUserDetails(administrator.getId(), administrator.getFirstName(),
                    administrator.getLastName(),
                    administrator.getUsername(), administrator.getPassword(), administrator.getEmail());
        } else {
            throw new InvalidRoleException();
        }
        return userDetails;
    }


    /**
     *
     * @param academicYearGradeSectionRoll
     * @return Enrollment
     * @throws UserDoesNotExistException if enrollment with year + grade + section + roll does not exist
     */
    public Student loadStudentForAuthentication(String academicYearGradeSectionRoll) {
        if (academicYearGradeSectionRoll == null || academicYearGradeSectionRoll.isBlank()) {
            throw new UserDoesNotExistException(ExceptionMessage.INVALID_USERNAME);
        }

        long count = academicYearGradeSectionRoll.chars().filter(c -> c == '_').count();

        if (count < 1) {
            throw new UserDoesNotExistException(ExceptionMessage.INVALID_USERNAME);
        }

        String year;
        String grade;
        String section;
        String roll;


        if (count == 2) {
            year = academicYearGradeSectionRoll.split("_")[0];
            grade = academicYearGradeSectionRoll.split("_")[1];
            section = "NONE";
            roll = academicYearGradeSectionRoll.split("_")[2];
        } else if (count == 3) {
            year = academicYearGradeSectionRoll.split("_")[0];
            grade = academicYearGradeSectionRoll.split("_")[1];
            section = academicYearGradeSectionRoll.split("_")[2];
            roll = academicYearGradeSectionRoll.split("_")[3];
        } else {
            throw new UserDoesNotExistException(ExceptionMessage.INVALID_USERNAME);
        }

        Optional<Student> student = studentRepository.findByGradeSectionRollAndYear(year, grade, section, roll);

        if (student.isPresent()) {
            return student.get();
        } else {
            throw new UserDoesNotExistException(ExceptionMessage.USER_DOES_NOT_EXIST);
        }
    }


    /**
     *
     * @param username
     * @return teacher
     * @throws UserDoesNotExistException if teacher with the given username does not exist
     */
    public Teacher loadTeacherForAuthentication(String username) {
        if (username == null || username.isBlank()) {
            throw new UserDoesNotExistException(ExceptionMessage.INVALID_USERNAME);
        }

        Optional<Teacher> teacher = teacherRepository.findByUsername(username);

        if (teacher.isPresent()) {
            return teacher.get();
        } else {
            throw new UserDoesNotExistException(ExceptionMessage.USER_DOES_NOT_EXIST);
        }
    }

    public Administrator loadAdministratorForAuthentication(String username) {
        if (username == null || username.isBlank()) {
            throw new UserDoesNotExistException(ExceptionMessage.INVALID_USERNAME);
        }

        Optional<Administrator> administrator = administratorRepository.findByUsername(username);

        if (administrator.isPresent()) {
            return administrator.get();
        } else {
            throw new UserDoesNotExistException(ExceptionMessage.USER_DOES_NOT_EXIST);
        }
    }
}
