package com.tu.java_spring_project.demo.dto.auth.teacher;

public record TeacherRegisterResponseDTO (
        Long id,
        String email,
        String activationToken
){
}
