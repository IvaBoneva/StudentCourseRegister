package com.tu.java_spring_project.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity // to save class in DB
@Table(name = "grades") // table in DB
@Data // auto-generated getters, setters, constructors and some methods
@NoArgsConstructor // empty constructor
public class Grade {
    @Id // primary key
    @Column(name = "grade_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-increment
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) // in JSON response, but not in POST / PUT
    private Long id;

    @DecimalMin(value = "2.0", inclusive = true)
    @DecimalMax(value = "6.0", inclusive = true)
    @Column(name = "grade_value", nullable = false)
    private Double gradeValue;

    // Дата на въвеждане
    @Column(name = "graded_at", nullable = false)
    private LocalDate gradedAt;

    // One Grade -> One Enrollment
    @OneToOne(mappedBy = "grade")
    private Enrollment enrollment;
}