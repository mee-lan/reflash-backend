package com.project.reflash.backend.dto;


import com.project.reflash.backend.auth.user_details.AdministratorUserDetails;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AdministratorDto {

    private Integer id;
    private String firstName;
    private String lastName;
    private String username;
    private String role = "ADMINISTRATOR";
    private String email;

    public AdministratorDto(Administrator administrator) {
        this.id = administrator.getId();
        this.firstName = administrator.getFirstName();
        this.lastName = administrator.getLastName();
        this.username = administrator.getUsername();
        this.email = administrator.getEmail();
    }

    public AdministratorDto(AdministratorUserDetails administrator ) {
        this.id = administrator.getId();
        this.firstName = administrator.getFirstName();
        this.lastName = administrator.getLastName();
        this.username = administrator.getUsername();
        this.email = administrator.getEmail();
    }
}
