package com.tu.java_spring_project.demo.dto;

import com.tu.java_spring_project.demo.enums.AcademicYear;

public record StudentRequestDto(
        String firstName,
        String lastName,
        String facultyNumber,
        String email,
        AcademicYear academicYear
) {
}
