package com.tu.java_spring_project.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity // to save class in DB
@Table(
        name = "enrollments",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"student_id", "course_id"})
        }
) // table in DB
@Data // auto-generated getters, setters, constructors and some methods
@NoArgsConstructor // empty constructor
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enrollment_id")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) // in JSON response, but not in POST / PUT
    private Long id; // Enrollment Id

    // Many Enrollments -> One Student
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    // Many Enrollments -> One Course
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    // Many Enrollments -> One Teacher
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @NotNull(message = "Grade value is required")
    @DecimalMin(value = "2.00", message = "Grade must be at least 2.00")
    @DecimalMax(value = "6.00", message = "Grade must not exceed 6.00")
    @Column(name = "grade_value", nullable = false)
    private Double gradeValue;

    @NotNull(message = "Graded date is required")
    @Column(name = "graded_at", nullable = false)
    private LocalDate gradedAt;
}