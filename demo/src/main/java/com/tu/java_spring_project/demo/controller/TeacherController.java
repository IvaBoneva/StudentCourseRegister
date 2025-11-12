package com.tu.java_spring_project.demo.controller;

import com.tu.java_spring_project.demo.dto.TeacherRequestDto;
import com.tu.java_spring_project.demo.dto.TeacherResponseDto;
import com.tu.java_spring_project.demo.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    // GET /api/teachers
    @GetMapping
    public List<TeacherResponseDto> getAllTeachers() {
        return teacherService.getAllTeachers();
    }

    // GET /api/teachers/{id}
    @GetMapping("/{id}")
    public TeacherResponseDto getTeacherById(@PathVariable Long id) {
        return teacherService.getTeacherByIdOrThrow(id);
    }

    // POST /api/teachers/save
    @PostMapping("/save")
    public ResponseEntity<TeacherResponseDto> saveTeacher(@RequestBody TeacherRequestDto dto) {
        TeacherResponseDto saved = teacherService.saveTeacher(dto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // PUT /api/teachers/{id}
    @PutMapping("/{id}")
    public ResponseEntity<TeacherResponseDto> updateTeacher(
            @PathVariable Long id,
            @RequestBody TeacherRequestDto dto) {
        TeacherResponseDto updated = teacherService.updateTeacher(id, dto);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    // DELETE /api/teachers/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return ResponseEntity.noContent().build();
    }
}
