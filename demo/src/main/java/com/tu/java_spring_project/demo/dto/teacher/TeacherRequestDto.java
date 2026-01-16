package com.tu.java_spring_project.demo.dto.teacher;

public record TeacherRequestDto(
        String firstName,
        String lastName,
        String email
) {
}
