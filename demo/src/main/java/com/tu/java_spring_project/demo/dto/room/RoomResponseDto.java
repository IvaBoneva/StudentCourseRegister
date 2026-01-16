package com.tu.java_spring_project.demo.dto.room;

import java.util.List;

public record RoomResponseDto(
        Long id,
        int capacity,
        List<String> courseNames
) {
}
