package com.tu.java_spring_project.demo.dto;

import java.time.LocalDate;

public record EnrollmentRequestDto(
        String studentFirstName,
        String studentLastName,
        String courseName,
        String teacherFirstName,
        String teacherLastName,
        Double gradeValue,
        LocalDate enrolledAt
) {}
