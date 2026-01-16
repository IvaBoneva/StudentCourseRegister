package com.tu.java_spring_project.demo.dto.course;

import java.util.List;

public record CourseResponseDto(
        Long id,
        String courseName,
        int credits,
        Long roomId,
        List<String> studentNames,
        List<String> teacherNames
) {
}
