package com.tu.java_spring_project.demo.service;

import com.tu.java_spring_project.demo.dto.EnrollmentRequestDto;
import com.tu.java_spring_project.demo.dto.EnrollmentResponseDto;
import com.tu.java_spring_project.demo.mapper.EnrollmentMapper;
import com.tu.java_spring_project.demo.model.*;
import com.tu.java_spring_project.demo.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class EnrollmentService {

    private final EnrollmentRepo enrollmentRepo;
    private final StudentRepo studentRepo;
    private final CourseRepo courseRepo;
    private final TeacherRepo teacherRepo;
    private final GradeRepo gradeRepo;
    private final EnrollmentMapper enrollmentMapper;  // инжектиран Mapper

    // Създаване на Enrollment
    public EnrollmentResponseDto createEnrollment(EnrollmentRequestDto dto) {
        Student student = studentRepo.findById(dto.studentId())
                .orElseThrow(() -> new NoSuchElementException("Student not found"));
        Course course = courseRepo.findById(dto.courseId())
                .orElseThrow(() -> new NoSuchElementException("Course not found"));

        // Проверка за Teacher
        Teacher teacher = null;
        if (dto.teacherId() != null) {
            teacher = teacherRepo.findById(dto.teacherId())
                    .orElseThrow(() -> new NoSuchElementException("Teacher not found"));
        }

        // Проверка за Grade
        Grade grade = null;
        if (dto.gradeId() != null) {
            grade = gradeRepo.findById(dto.gradeId())
                    .orElseThrow(() -> new NoSuchElementException("Grade not found"));
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setTeacher(teacher);  // сетва учителя
        enrollment.setGrade(grade);      // сетва оценката
        enrollment.setEnrolledAt(dto.enrolledAt());

        Enrollment saved = enrollmentRepo.save(enrollment);
        return enrollmentMapper.toDto(saved);
    }


    // Връща всички Enrollment-и
    public List<EnrollmentResponseDto> getAllEnrollments() {
        return enrollmentRepo.findAll()
                .stream()
                .map(enrollmentMapper::toDto)
                .toList();
    }

    // Връща Enrollment по ID
    public EnrollmentResponseDto getEnrollmentById(Long id) {
        Enrollment enrollment = enrollmentRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Enrollment not found with id " + id));
        return enrollmentMapper.toDto(enrollment);
    }

}
