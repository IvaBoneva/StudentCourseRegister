package com.tu.java_spring_project.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tu.java_spring_project.demo.enums.AcademicYear;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotBlank(message = "Faculty number is required")
    @Pattern(
            regexp = "\\d{9}",
            message = "Faculty number must contain exactly 9 digits"
    )
    @Column(name = "faculty_number", nullable = false, unique = true, length = 9)
    private String facultyNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Column(nullable = false, unique = true)
    private String email;

    @NotNull(message = "Academic year is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "academic_year", nullable = false)
    private AcademicYear academicYear;

    // One Student -> Many Enrollments
    @OneToMany(mappedBy = "student")
    private List<Enrollment> enrollments;
}
