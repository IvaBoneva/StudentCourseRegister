package com.tu.java_spring_project.demo.dto.auth.teacher;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TeacherRegisterRequestDTO (
        @NotBlank(message = "Teacher first name cannot be null or empty")
        @Size(min = 2, max = 20, message = "Employee first name must be between 2 and 20 characters")
        String firstName,

        @NotBlank(message = "Teacher last name cannot be null or empty")
        @Size(min = 2, max = 20, message = "Employee last name must be between 2 and 20 characters")
        String lastName,

        @NotBlank
        @Email
        String email
) {
}
