package com.tu.java_spring_project.demo.service;

import com.tu.java_spring_project.demo.dto.RoomRequestDto;
import com.tu.java_spring_project.demo.dto.RoomResponseDto;
import com.tu.java_spring_project.demo.mapper.RoomMapper;
import com.tu.java_spring_project.demo.model.Room;
import com.tu.java_spring_project.demo.repository.RoomRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomService {

    private final RoomRepo roomRepo;
    private final RoomMapper roomMapper;

    // Връща лист с всички стаи
    public List<RoomResponseDto> getAllRooms() {
        return roomMapper.toRoomResponseDtoList(roomRepo.findAll());
    }

    // Връща стая по id
    public RoomResponseDto getRoomByIdOrThrow(Long id) {
        Room room = roomRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Room not found with id " + id));
        return roomMapper.toRoomResponseDto(room);
    }

    // Създава и запазва стая
    @Transactional
    public RoomResponseDto saveRoom(RoomRequestDto dto) {
        Room room = roomMapper.toRoom(dto);
        room.setCourses(new ArrayList<>());
        Room saved = roomRepo.save(room);
        return roomMapper.toRoomResponseDto(saved);
    }

    // Редактира стая по id
    @Transactional
    public RoomResponseDto updateRoom(Long id, RoomRequestDto dto) {
        Room existing = roomRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Room not found with id " + id));

        existing.setCapacity(dto.capacity());

        Room updated = roomRepo.save(existing);
        return roomMapper.toRoomResponseDto(updated);
    }

    // Трие стая по id
    @Transactional
    public void deleteRoom(Long id) {
        if (!roomRepo.existsById(id)) {
            throw new NoSuchElementException("Room not found with id " + id);
        }
        roomRepo.deleteById(id);
    }
}
