package com.tu.java_spring_project.demo.dto.auth.student;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record StudentLoginRequestDTO(
        @Schema(description = "User's faculty number ", example = "123456789")
        @NotBlank
        @Pattern(regexp = "\\d{9}", message = "Faculty number must contain exactly 9 digits")
        String facultyNumber,

        @Schema(description = "User's password", example = "myPass1234")
        @NotBlank
        @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters")
        @Pattern(regexp = ".*\\d.*", message = "Password must contain at least one number")
        String password
) {
}
