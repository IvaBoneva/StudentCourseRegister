package com.tu.java_spring_project.demo.dto;

import java.util.List;

public record CourseRequestDto(
        String courseName,
        int credits,
        Long roomId
) {
}
