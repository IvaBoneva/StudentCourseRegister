package com.tu.java_spring_project.demo.dto.student;

import com.tu.java_spring_project.demo.enums.AcademicYear;
import java.util.List;

public record StudentResponseDto(
        Long id,
        String firstName,
        String lastName,
        String facultyNumber,
        String email,
        AcademicYear academicYear,
        List<String> courseNames
) {
}
