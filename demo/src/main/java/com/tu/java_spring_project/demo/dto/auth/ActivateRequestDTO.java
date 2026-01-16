package com.tu.java_spring_project.demo.dto.auth;

public record ActivateRequestDTO(
        String token,
        String password
) {
}
