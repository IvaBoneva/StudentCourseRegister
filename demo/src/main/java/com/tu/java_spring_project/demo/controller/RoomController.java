package com.tu.java_spring_project.demo.controller;

import com.tu.java_spring_project.demo.dto.room.RoomRequestDto;
import com.tu.java_spring_project.demo.dto.room.RoomResponseDto;
import com.tu.java_spring_project.demo.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms")
@PreAuthorize("hasRole('ADMIN')")
public class RoomController {

    private final RoomService roomService;

    // GET /api/rooms
    @GetMapping
    public List<RoomResponseDto> getAllRooms() {
        return roomService.getAllRooms();
    }

    // GET /api/rooms/{id}
    @GetMapping("/{id}")
    public RoomResponseDto getRoomById(@PathVariable Long id) {
        return roomService.getRoomByIdOrThrow(id);
    }

    // POST /api/rooms/save
    @PostMapping("/save")
    public ResponseEntity<RoomResponseDto> saveRoom(@RequestBody RoomRequestDto dto) {
        RoomResponseDto saved = roomService.saveRoom(dto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // PUT /api/rooms/{id}
    @PutMapping("/{id}")
    public ResponseEntity<RoomResponseDto> updateRoom(
            @PathVariable Long id,
            @RequestBody RoomRequestDto dto) {
        RoomResponseDto updated = roomService.updateRoom(id, dto);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    // DELETE /api/rooms/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }
}
