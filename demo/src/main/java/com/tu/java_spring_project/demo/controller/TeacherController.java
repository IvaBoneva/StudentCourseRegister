package com.tu.java_spring_project.demo.controller;

import com.tu.java_spring_project.demo.dto.teacher.TeacherRequestDto;
import com.tu.java_spring_project.demo.dto.teacher.TeacherResponseDto;
import com.tu.java_spring_project.demo.mapper.TeacherMapper;
import com.tu.java_spring_project.demo.model.Role;
import com.tu.java_spring_project.demo.model.Teacher;
import com.tu.java_spring_project.demo.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teachers")
public class TeacherController {

    private final TeacherService teacherService;
    private final TeacherMapper teacherMapper;

    // GET /api/teachers (ADMIN only)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<TeacherResponseDto> getAllTeachers() {
        return teacherService.getAllTeachers();
    }

    // GET /api/teachers/{id} (ADMIN or owner)
    @PreAuthorize("hasRole('ADMIN') or @enrollmentSecurity.canAccessTeacher(principal, #id)")
    @GetMapping("/{id}")
    public TeacherResponseDto getTeacherById(@PathVariable Long id) {
        return teacherService.getTeacherByIdOrThrow(id);
    }

    // PUT /api/teachers/{id} (ADMIN only)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<TeacherResponseDto> updateTeacher(
            @PathVariable Long id,
            @RequestBody TeacherRequestDto dto) {

        TeacherResponseDto updated = teacherService.updateTeacher(id, dto);
        return ResponseEntity.ok(updated);
    }

    // DELETE /api/teachers/{id} (ADMIN only)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return ResponseEntity.noContent().build();
    }

    // PATCH /api/teachers/{id}/role (ADMIN only)
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/role")
    public ResponseEntity<TeacherResponseDto> changeTeacherRole(
            @PathVariable Long id,
            @RequestParam Role role) {

        Teacher updated = teacherService.patchTeacherRole(id, role);
        return ResponseEntity.ok(teacherMapper.toTeacherResponseDto(updated));
    }
}
