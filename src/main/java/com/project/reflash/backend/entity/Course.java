package com.project.reflash.backend.entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "courses")
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="grade_name", nullable = false)
    String gradeName;

    @Column(name="academic_year", nullable=false)
    String academicYear;
}
