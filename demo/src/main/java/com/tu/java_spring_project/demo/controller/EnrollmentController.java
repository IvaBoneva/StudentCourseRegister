package com.tu.java_spring_project.demo.controller;

import com.tu.java_spring_project.demo.dto.enrollment.EnrollmentGradeUpdateDto;
import com.tu.java_spring_project.demo.dto.enrollment.EnrollmentRequestDto;
import com.tu.java_spring_project.demo.dto.enrollment.EnrollmentResponseDto;
import com.tu.java_spring_project.demo.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/save")
    public ResponseEntity<EnrollmentResponseDto> createEnrollment(
            @RequestBody EnrollmentRequestDto dto) {
        return new ResponseEntity<>(
                enrollmentService.createEnrollment(dto),
                HttpStatus.CREATED
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<EnrollmentResponseDto>> getAllEnrollments() {
        return ResponseEntity.ok(enrollmentService.getAllEnrollments());
    }

    @PreAuthorize("""
    hasRole('ADMIN')
    or @enrollmentSecurity.isTeacherOfEnrollment(principal.teacherId, #id)
    or @enrollmentSecurity.isStudentOfEnrollment(principal.studentId, #id)
""")
    @GetMapping("/{id}")
    public EnrollmentResponseDto getEnrollmentById(@PathVariable Long id) {
        return enrollmentService.getEnrollmentById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    // DELETE /api/enrollments/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable Long id) {
        enrollmentService.deleteEnrollment(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN') or @enrollmentSecurity.isTeacherOfEnrollment(principal.teacherId, #id)")
    @PutMapping("/{id}")
    public ResponseEntity<EnrollmentResponseDto> updateGrade(
            @PathVariable Long id,
            @RequestBody EnrollmentGradeUpdateDto dto) {
        EnrollmentResponseDto updated = enrollmentService.updateGrade(id, dto);
        return ResponseEntity.ok(updated);
    }

}