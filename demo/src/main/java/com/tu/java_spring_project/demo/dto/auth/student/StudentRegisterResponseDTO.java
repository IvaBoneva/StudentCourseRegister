package com.tu.java_spring_project.demo.dto.auth.student;

public record StudentRegisterResponseDTO(
        Long id,
        String facultyNumber,
        String email,
        String activationToken
) {
}
