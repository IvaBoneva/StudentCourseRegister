package com.tu.java_spring_project.demo.service_layer_tests;

import com.tu.java_spring_project.demo.dto.enrollment.EnrollmentRequestDto;
import com.tu.java_spring_project.demo.mapper.EnrollmentMapper;
import com.tu.java_spring_project.demo.model.*;
import com.tu.java_spring_project.demo.repository.CourseRepo;
import com.tu.java_spring_project.demo.repository.EnrollmentRepo;
import com.tu.java_spring_project.demo.repository.StudentRepo;
import com.tu.java_spring_project.demo.repository.TeacherRepo;
import com.tu.java_spring_project.demo.service.EnrollmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.*;

class EnrollmentServiceTest {

    private EnrollmentRepo enrollmentRepo;
    private StudentRepo studentRepo;
    private TeacherRepo teacherRepo;
    private CourseRepo courseRepo;
    private EnrollmentMapper enrollmentMapper;

    private EnrollmentService enrollmentService;

    @BeforeEach
    void setUp() {
        enrollmentRepo = mock(EnrollmentRepo.class);
        studentRepo = mock(StudentRepo.class);
        teacherRepo = mock(TeacherRepo.class);
        courseRepo = mock(CourseRepo.class);
        enrollmentMapper = mock(EnrollmentMapper.class);

        enrollmentService = new EnrollmentService(
                enrollmentRepo,
                studentRepo,
                courseRepo,
                teacherRepo,
                enrollmentMapper
        );
    }

    @Test
    void createEnrollment_shouldCreateEnrollmentSuccessfully() {
        // Arrange
        Student student = new Student();
        student.setFacultyNumber("980071661");

        Course course = new Course();
        course.setCourseName("AI Learning");

        Room room = new Room();
        room.setCapacity(30);
        course.setRoom(room);

        Teacher teacher = new Teacher();
        teacher.setId(1L);

        Enrollment enrollment = new Enrollment();

        when(studentRepo.findStudentByFacultyNumber("980071661"))
                .thenReturn(Optional.of(student));
        when(courseRepo.findCourseByName("AI Learning"))
                .thenReturn(Optional.of(course));
        when(teacherRepo.findById(1L))
                .thenReturn(Optional.of(teacher));
        when(enrollmentRepo.save(any(Enrollment.class)))
                .thenReturn(enrollment);

        EnrollmentRequestDto dto = new EnrollmentRequestDto(
                "980071661", "AI Learning",
                1L,  // teacherId
                5.50,
                LocalDate.now()
        );

        // Act
        enrollmentService.createEnrollment(dto);

        // Assert / Verify
        verify(enrollmentRepo, times(1)).save(any(Enrollment.class));
    }
}