package com.tu.java_spring_project.demo.controller;
import com.tu.java_spring_project.demo.dto.EnrollmentRequestDto;
import com.tu.java_spring_project.demo.dto.EnrollmentResponseDto;
import com.tu.java_spring_project.demo.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    // Създаване на Enrollment
    @PostMapping("/save")
    public ResponseEntity<EnrollmentResponseDto> createEnrollment(
            @RequestBody EnrollmentRequestDto dto) {
        EnrollmentResponseDto response = enrollmentService.createEnrollment(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Получаване на всички Enrollment-и
    @GetMapping
    public ResponseEntity<List<EnrollmentResponseDto>> getAllEnrollments() {
        List<EnrollmentResponseDto> enrollments = enrollmentService.getAllEnrollments();
        return ResponseEntity.ok(enrollments);
    }

    // Получаване на Enrollment по id
    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentResponseDto> getEnrollmentById(@PathVariable Long id) {
        EnrollmentResponseDto enrollment = enrollmentService.getEnrollmentById(id);
        return ResponseEntity.ok(enrollment);
    }


}

