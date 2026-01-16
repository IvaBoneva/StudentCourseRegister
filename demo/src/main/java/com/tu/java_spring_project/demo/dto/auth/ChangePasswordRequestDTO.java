package com.tu.java_spring_project.demo.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequestDTO(
        @NotBlank String identifier,   // email for teacher, facultyNumber for student
        @NotBlank String oldPassword,
        @NotBlank @Size(min = 6, message = "New password must be at least 6 characters") String newPassword
) {}


