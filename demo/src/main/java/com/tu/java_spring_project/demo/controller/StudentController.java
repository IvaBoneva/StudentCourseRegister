package com.tu.java_spring_project.demo.controller;

import com.tu.java_spring_project.demo.dto.student.StudentRequestDto;
import com.tu.java_spring_project.demo.dto.student.StudentResponseDto;
import com.tu.java_spring_project.demo.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    // GET /api/students (ADMIN only)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<StudentResponseDto> getAllStudents() {
        return studentService.getAllStudents();
    }

    @PreAuthorize("""
        hasRole('ADMIN')
        or @enrollmentSecurity.canAccessStudent(principal.teacherId, #id)
        or @enrollmentSecurity.isSelfStudent(#id, principal.studentId)
    """)
    @GetMapping("/{id}")
    public StudentResponseDto getStudentById(@PathVariable Long id) {
        return studentService.getStudentByIdOrThrow(id);
    }

    // PUT /api/students/{id}
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<StudentResponseDto> updateStudent(
            @PathVariable Long id,
            @RequestBody StudentRequestDto dto) {
        StudentResponseDto updated = studentService.updateStudent(id, dto);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    // DELETE /api/students/{id}
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}
