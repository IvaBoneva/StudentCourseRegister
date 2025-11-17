package com.tu.java_spring_project.demo.dto;

import java.time.LocalDate;

public record GradeRequestDto(
        Double gradeValue,
        LocalDate gradedAt
) {
}
