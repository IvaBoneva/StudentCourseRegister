package com.tu.java_spring_project.demo.dto;

import java.time.LocalDate;

public record EnrollmentRequestDto(
        Long studentId,
        Long courseId,
        Long teacherId,
        Long gradeId,
        LocalDate enrolledAt
) {}
