package com.tu.java_spring_project.demo.mapper;

import com.tu.java_spring_project.demo.dto.RoomRequestDto;
import com.tu.java_spring_project.demo.dto.RoomResponseDto;
import com.tu.java_spring_project.demo.model.Room;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-21T23:22:08+0200",
    comments = "version: 1.6.0, compiler: javac, environment: Java 17.0.16 (Eclipse Adoptium)"
)
@Component
public class RoomMapperImpl implements RoomMapper {

    @Override
    public RoomResponseDto toRoomResponseDto(Room room) {
        if ( room == null ) {
            return null;
        }

        List<String> courseNames = null;
        Long id = null;
        int capacity = 0;

        courseNames = mapCoursesToCourseNames( room.getCourses() );
        id = room.getId();
        capacity = room.getCapacity();

        RoomResponseDto roomResponseDto = new RoomResponseDto( id, capacity, courseNames );

        return roomResponseDto;
    }

    @Override
    public List<RoomResponseDto> toRoomResponseDtoList(List<Room> rooms) {
        if ( rooms == null ) {
            return null;
        }

        List<RoomResponseDto> list = new ArrayList<RoomResponseDto>( rooms.size() );
        for ( Room room : rooms ) {
            list.add( toRoomResponseDto( room ) );
        }

        return list;
    }

    @Override
    public Room toRoom(RoomRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        Room room = new Room();

        room.setCapacity( dto.capacity() );

        return room;
    }
}
