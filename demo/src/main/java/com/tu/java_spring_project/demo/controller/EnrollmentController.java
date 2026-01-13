package com.tu.java_spring_project.demo.controller;

import com.tu.java_spring_project.demo.dto.EnrollmentGradeUpdateDto;
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

    @PostMapping("/save")
    public ResponseEntity<EnrollmentResponseDto> createEnrollment(
            @RequestBody EnrollmentRequestDto dto) {
        return new ResponseEntity<>(
                enrollmentService.createEnrollment(dto),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<List<EnrollmentResponseDto>> getAllEnrollments() {
        return ResponseEntity.ok(enrollmentService.getAllEnrollments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentResponseDto> getEnrollmentById(@PathVariable Long id) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentById(id));
    }

    // DELETE /api/enrollments/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable Long id) {
        enrollmentService.deleteEnrollment(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<EnrollmentResponseDto> updateGrade(
            @PathVariable Long id,
            @RequestBody EnrollmentGradeUpdateDto dto) {
        EnrollmentResponseDto updated = enrollmentService.updateGrade(id, dto);
        return ResponseEntity.ok(updated);
    }

}