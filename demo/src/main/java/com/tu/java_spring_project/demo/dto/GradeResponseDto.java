package com.tu.java_spring_project.demo.dto;

import java.time.LocalDate;

public record GradeResponseDto(
        Long id,
        Double gradeValue,
        LocalDate gradedAt,
        String studentName,
        String courseName
) {
}
