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
            throw new IllegalArgumentException("Graded date is required");
        }

        if (dto.gradedAt().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Graded date cannot be after date now");
        }

        // Колко курса води учителят
        long distinctCourseCount = enrollmentRepo.findAll().stream()
                .filter(e -> e.getTeacher().getId().equals(teacher.getId()))
                .map(e -> e.getCourse().getId())
                .distinct().count();

        // Ако учителят не води подаденият курс
        boolean isNewCourseForTeacher = enrollmentRepo.findAll().stream()
                .noneMatch(e -> e.getTeacher().getId().equals(teacher.getId())
                        && e.getCourse().getId().equals(course.getId()));

        if (isNewCourseForTeacher && distinctCourseCount >= 3) {
            throw new IllegalArgumentException("Teacher cannot teach more than 3 courses");
        }

        // Проверка дали стаята е пълна
        long studentsInCourse = course.getEnrollments().stream().count();

        if(studentsInCourse >= course.getRoom().getCapacity())
            throw new RuntimeException("Room is full");

        // Проверка дали студентът вече има оценка по този предмет
        boolean studentAlreadyGraded = enrollmentRepo.findAll().stream()
                .anyMatch(e -> e.getStudent().getId().equals(student.getId())
                        && e.getCourse().getId().equals(course.getId()));

        if (studentAlreadyGraded){
            throw new IllegalArgumentException("Student already has a grade for this course");
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

        if(newGradedAt == null) {
            throw new IllegalArgumentException("New graded date cannot be null");
        }

        if(newGradedAt.isBefore(enrollment.getGradedAt())) {
            throw new IllegalArgumentException("New graded date cannot be before the old graded date");
        }

        if(newGradedAt.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("New graded date cannot be after date now");
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
