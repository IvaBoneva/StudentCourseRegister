package com.tu.java_spring_project.demo.dto.teacher;

import java.util.List;

public record TeacherResponseDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        List<String> courseNames
) {
}
