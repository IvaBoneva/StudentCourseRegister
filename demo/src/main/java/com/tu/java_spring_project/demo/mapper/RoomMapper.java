package com.tu.java_spring_project.demo.mapper;

import com.tu.java_spring_project.demo.dto.room.RoomRequestDto;
import com.tu.java_spring_project.demo.dto.room.RoomResponseDto;
import com.tu.java_spring_project.demo.model.Course;
import com.tu.java_spring_project.demo.model.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoomMapper {


    @Mapping(target = "courseNames", source = "courses")
    RoomResponseDto toRoomResponseDto(Room room);

    List<RoomResponseDto> toRoomResponseDtoList(List<Room> rooms);
    Room toRoom(RoomRequestDto dto);

    // Мапване на courses → courseNames
    default List<String> mapCoursesToCourseNames(List<Course> courses) {
        if (courses == null) return null;
        return courses.stream()
                .map(course -> course.getName())
                .collect(Collectors.toList());
    }


}
