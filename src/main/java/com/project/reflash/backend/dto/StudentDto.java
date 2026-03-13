package com.project.reflash.backend.dto;

import com.project.reflash.backend.auth.user_details.StudentUserDetails;
import com.project.reflash.backend.entity.Student;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentDto {
    private Integer id;
    private String firstName;
    private String lastName;
    private String grade;
    private String section;
    private String roll;
    private String academicYear;
    private String role = "STUDENT";

    public StudentDto(Student student) {
        this.id = student.getId();
        this.firstName = student.getFirstName();
        this.lastName = student.getLastName();
        this.grade = student.getGrade();
        this.section = student.getSection();
        this.roll = student.getRoll();
        this.academicYear = student.getAcademicYear();
    }


    public StudentDto(StudentUserDetails student) {
        this.id = student.getId();
        this.firstName = student.getFirstName();
        this.lastName = student.getLastName();
        this.grade = student.getGrade();
        this.section = student.getSection();
        this.roll = student.getRoll();
        this.academicYear = student.getAcademicYear();
    }
}
