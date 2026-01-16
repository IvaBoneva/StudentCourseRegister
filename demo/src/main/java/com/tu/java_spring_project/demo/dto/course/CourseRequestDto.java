package com.tu.java_spring_project.demo.dto.course;

public record CourseRequestDto(
        String courseName,
        int credits,
        Long roomId
) {
}
