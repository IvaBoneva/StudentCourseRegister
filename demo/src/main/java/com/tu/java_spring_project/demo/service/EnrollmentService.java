package com.tu.java_spring_project.demo.service;

import com.tu.java_spring_project.demo.dto.EnrollmentGradeUpdateDto;
import com.tu.java_spring_project.demo.dto.EnrollmentRequestDto;
import com.tu.java_spring_project.demo.dto.EnrollmentResponseDto;
import com.tu.java_spring_project.demo.mapper.EnrollmentMapper;
import com.tu.java_spring_project.demo.model.*;
import com.tu.java_spring_project.demo.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EnrollmentService {

    private final EnrollmentRepo enrollmentRepo;
    private final StudentRepo studentRepo;
    private final CourseRepo courseRepo;
    private final TeacherRepo teacherRepo;
    private final EnrollmentMapper enrollmentMapper;

    public EnrollmentResponseDto createEnrollment(EnrollmentRequestDto dto) {

        // Намери студент по faculty number
        Student student = studentRepo.findStudentByFacultyNumber(dto.studentFacultyNumber())
                .orElseThrow(() -> new IllegalArgumentException("Student does not exist"));

        // Намери учител по ID
        Teacher teacher = teacherRepo.findById(dto.teacherId())
                .orElseThrow(() -> new IllegalArgumentException("Teacher does not exist"));

        // Намери курс по име
        Course course = courseRepo.findCourseByName(dto.courseName())
                .orElseThrow(() -> new IllegalArgumentException("Course does not exist"));

        // Проверка gradedAt
        if (dto.gradedAt() == null) {
            throw new IllegalArgumentException("gradedAt is required");
        }

        // ENROLLMENT
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setTeacher(teacher);
        enrollment.setCourse(course);
        enrollment.setGradeValue(dto.gradeValue());


        // Задаваме gradedAt
        enrollment.setGradedAt(dto.gradedAt());

        // Save Enrollment
        Enrollment saved = enrollmentRepo.save(enrollment);

        return enrollmentMapper.toDto(saved);
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

    public void deleteEnrollment(Long id) {

        Enrollment enrollment = enrollmentRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Enrollment not found with id " + id
                ));
        enrollmentRepo.delete(enrollment);

    }

    public EnrollmentResponseDto updateGrade(Long enrollmentId, EnrollmentGradeUpdateDto dto) {
        // Намираме запис
        Enrollment enrollment = enrollmentRepo.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Enrollment not found with id " + enrollmentId
                ));

        Double newGrade = dto.gradeValue();
        LocalDate newGradedAt = dto.gradedAt();

        // Проверка валидност на оценката
        if (newGrade == null || newGrade < 2.0 || newGrade > 6.0) {
            throw new IllegalArgumentException("Grade must be between 2.00 and 6.00");
        }

        // Проверка дали са минали 4 месеца
        LocalDate fourMonthsLater = enrollment.getGradedAt().plusMonths(4);
        if (LocalDate.now().isAfter(fourMonthsLater)) {
            throw new IllegalArgumentException(
                    "Cannot update grade after 4 months from gradedAt"
            );
        }

        // Актуализиране
        enrollment.setGradeValue(newGrade);
        enrollment.setGradedAt(newGradedAt);
        Enrollment saved = enrollmentRepo.save(enrollment);

        return enrollmentMapper.toDto(saved);
    }



}
