package com.tu.java_spring_project.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "grades")
@Data
@NoArgsConstructor
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "grade_id")
    private Long id;

    @Column(name = "grade_value", nullable = false)
    private Double gradeValue;

    @Column(name = "graded_at", nullable = false)
    private LocalDate gradedAt;

    @OneToOne(mappedBy = "grade")
    private Enrollment enrollment;

    // ТОЗИ constructor оправя null-а
    public Grade(Double gradeValue) {
        this.gradeValue = gradeValue;
        this.gradedAt = LocalDate.now();
    }
}
