package com.tu.java_spring_project.demo.dto.enrollment;

import java.time.LocalDate;

public record EnrollmentGradeUpdateDto(
        Double gradeValue,
        LocalDate gradedAt
) {
}
