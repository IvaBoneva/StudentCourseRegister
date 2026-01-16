package com.tu.java_spring_project.demo.service_layer_tests;

import com.tu.java_spring_project.demo.config.security.StudentPrincipal;
import com.tu.java_spring_project.demo.config.security.TeacherPrincipal;
import com.tu.java_spring_project.demo.model.Student;
import com.tu.java_spring_project.demo.model.Teacher;
import com.tu.java_spring_project.demo.repository.StudentRepo;
import com.tu.java_spring_project.demo.repository.TeacherRepo;
import com.tu.java_spring_project.demo.service.AppUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppUserDetailsServiceTest {

    @InjectMocks
    private AppUserDetailsService userDetailsService;

    @Mock
    private TeacherRepo teacherRepo;

    @Mock
    private StudentRepo studentRepo;

    private Teacher teacher;
    private Student student;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        teacher = Teacher.builder()
                .id(1L)
                .email("teacher@example.com")
                .build();

        student = Student.builder()
                .id(2L)
                .facultyNumber("123456789")
                .build();
    }

    @Test
    void loadUserByUsername_ShouldReturnTeacherPrincipal() {
        when(teacherRepo.findTeacherByEmail("teacher@example.com")).thenReturn(Optional.of(teacher));

        UserDetails userDetails = userDetailsService.loadUserByUsername("teacher@example.com");

        assertTrue(userDetails instanceof TeacherPrincipal);
        assertEquals("teacher@example.com", userDetails.getUsername());
    }

    @Test
    void loadUserByUsername_ShouldReturnStudentPrincipal() {
        when(teacherRepo.findTeacherByEmail("student@example.com")).thenReturn(Optional.empty());
        when(studentRepo.findStudentByFacultyNumber("123456789")).thenReturn(Optional.of(student));

        UserDetails userDetails = userDetailsService.loadUserByUsername("123456789");

        assertTrue(userDetails instanceof StudentPrincipal);
        assertEquals("123456789", userDetails.getUsername());
    }

    @Test
    void loadUserByUsername_UserNotFound_ShouldThrow() {
        // Simulate a non-existing teacher email and student faculty number
        String fakeEmail = "fake_teacher@example.com";
        String fakeFacultyNumber = "999999999";

        when(teacherRepo.findTeacherByEmail(fakeEmail)).thenReturn(Optional.empty());
        when(studentRepo.findStudentByFacultyNumber(fakeFacultyNumber)).thenReturn(Optional.empty());

        UsernameNotFoundException ex = assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(fakeEmail) // you could also try fakeFacultyNumber
        );

        assertEquals("User not found", ex.getMessage());
    }

}

