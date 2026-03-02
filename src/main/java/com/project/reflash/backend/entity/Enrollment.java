package com.project.reflash.backend.entity;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "enrollments",
        uniqueConstraints = @UniqueConstraint(columnNames = {"grade", "section", "roll", "academic_year"}))
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY) // Many enrollments can belong to one student
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(name = "grade", nullable = false)
    private String grade;

    @Column(name = "section", nullable = false)
    private String section = "NONE";

    @Column(name = "roll", nullable = false)
    private String roll;

    @Column(name = "academic_year", nullable = false)
    private String academicYear;
}
