package com.project.reflash.backend.dto;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "administrators")
@AllArgsConstructor
@Builder
@NoArgsConstructor

public class Administrator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "firstname", nullable=false)
    String firstName;

    @Column(name = "lastname", nullable=false)
    String lastName;

    @Column(name = "username", nullable = false)
    String username;

    @Column(name = "password", nullable=false)
    String password;

    @Column(name = "email", nullable=false)
    String email;
}
