package com.tu.java_spring_project.demo.dto.auth.student;

import com.tu.java_spring_project.demo.enums.AcademicYear;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record StudentRegisterRequestDTO(
        @NotBlank(message = "Student first name cannot be null or empty")
        @Size(min = 2, max = 20, message = "Employee first name must be between 2 and 20 characters")
        String firstName,

        @NotBlank(message = "Student last name cannot be null or empty")
        @Size(min = 2, max = 20, message = "Employee last name must be between 2 and 20 characters")
        String lastName,

        @NotBlank(message = "Faculty number is required")
        @Pattern(
                regexp = "\\d{9}",
                message = "Faculty number must contain exactly 9 digits"
        )
        String facultyNumber,

        @NotBlank
        @Email
        String email,

        AcademicYear academicYear
) {
}
