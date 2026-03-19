package com.project.reflash.backend.dto;

import com.project.reflash.backend.auth.user_details.TeacherUserDetails;
import com.project.reflash.backend.entity.Teacher;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class TeacherDto {
    private Integer id;
    private String firstName;
    private String lastName;
    private String username;
    private String role = "TEACHER";
    private String email;

    public TeacherDto(Teacher teacher) {
        this.id = teacher.getId();
        this.firstName = teacher.getFirstName();
        this.lastName = teacher.getLastName();
        this.username = teacher.getUsername();
        this.email = teacher.getEmail();
    }

    public TeacherDto(TeacherUserDetails teacher ) {
        this.id = teacher.getId();
        this.firstName = teacher.getFirstName();
        this.lastName = teacher.getLastName();
        this.username = teacher.getUsername();
        this.email = teacher.getEmail();
    }
}
