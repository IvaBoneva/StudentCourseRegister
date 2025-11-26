package com.tu.java_spring_project.demo.dto;

import java.time.LocalDate;

public record EnrollmentResponseDto(
        Long id,
        String studentName,
        String courseName,
        String teacherName,
        Double gradeValue,
        LocalDate enrolledAt
) {}
