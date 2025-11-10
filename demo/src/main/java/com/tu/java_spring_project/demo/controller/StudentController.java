package com.tu.java_spring_project.demo.controller;

import com.tu.java_spring_project.demo.dto.StudentRequestDto;
import com.tu.java_spring_project.demo.dto.StudentResponseDto;
import com.tu.java_spring_project.demo.model.Student;
import com.tu.java_spring_project.demo.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    // GET /api/students
    @GetMapping
    public List<StudentResponseDto> getAllStudents() {
        return studentService.getAllStudents();
    }

    // GET /api/students/{id}
    @GetMapping("/{id}")
    public StudentResponseDto getStudentById(@PathVariable Long id) {
        return studentService.getStudentByIdOrThrow(id);
    }

    // POST /api/students/save
    @PostMapping("/save")
    public ResponseEntity<StudentResponseDto> saveStudent(@RequestBody StudentRequestDto dto) {
        StudentResponseDto saved = studentService.saveStudent(dto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // PUT /api/students/{id}
    @PutMapping("/{id}")
    public ResponseEntity<StudentResponseDto> updateStudent(
            @PathVariable Long id,
            @RequestBody StudentRequestDto dto) {
        StudentResponseDto updated = studentService.updateStudent(id, dto);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    // DELETE /api/students/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}
