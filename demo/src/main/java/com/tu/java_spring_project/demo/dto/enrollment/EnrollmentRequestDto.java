package com.tu.java_spring_project.demo.dto.enrollment;

import java.time.LocalDate;

public record EnrollmentRequestDto(
        String studentFacultyNumber,
        String courseName,
        Long teacherId,
        Double gradeValue,
        LocalDate gradedAt
) {}
