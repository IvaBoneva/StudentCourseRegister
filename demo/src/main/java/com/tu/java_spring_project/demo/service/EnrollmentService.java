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

@Service
@RequiredArgsConstructor
@Transactional
public class EnrollmentService {

    private final EnrollmentRepo enrollmentRepo;
    private final StudentRepo studentRepo;
    private final CourseRepo courseRepo;
    private final TeacherRepo teacherRepo;
    private final GradeRepo gradeRepo;
    private final EnrollmentMapper enrollmentMapper;

    public EnrollmentResponseDto createEnrollment(EnrollmentRequestDto dto) {

        Student student = studentRepo
                .findStudentByFullName(dto.studentFirstName(), dto.studentLastName())
                .orElseThrow(() -> new IllegalArgumentException("Student does not exist"));

        Teacher teacher = teacherRepo
                .findTeacherByFullName(dto.teacherFirstName(), dto.teacherLastName())
                .orElseThrow(() -> new IllegalArgumentException("Teacher does not exist"));

        Course course = courseRepo
                .findCourseByName(dto.courseName())
                .orElseThrow(() -> new IllegalArgumentException("Course does not exist"));


        // GRADE – създаваме, защото е част от Enrollment
        if (dto.gradeValue() == null) {
            throw new IllegalArgumentException("Grade value cannot be null");
        }

        Grade grade = new Grade(dto.gradeValue());
        gradeRepo.save(grade);

        // ENROLLMENT
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setTeacher(teacher);
        enrollment.setGrade(grade);
        enrollment.setEnrolledAt(dto.enrolledAt());

        return enrollmentMapper.toDto(
                enrollmentRepo.save(enrollment)
        );
    }

    public List<EnrollmentResponseDto> getAllEnrollments() {
        return enrollmentRepo.findAll()
                .stream()
                .map(enrollmentMapper::toDto)
                .toList();
    }

    public EnrollmentResponseDto getEnrollmentById(Long id) {
        return enrollmentRepo.findById(id)
                .map(enrollmentMapper::toDto)
                .orElseThrow(() ->
                        new RuntimeException("Enrollment not found")
                );
    }
}
