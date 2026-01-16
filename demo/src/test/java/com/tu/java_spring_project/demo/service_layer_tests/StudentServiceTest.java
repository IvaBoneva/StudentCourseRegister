package com.tu.java_spring_project.demo.service_layer_tests;

import com.tu.java_spring_project.demo.dto.auth.student.StudentRegisterRequestDTO;
import com.tu.java_spring_project.demo.exception.DuplicateResourceException;
import com.tu.java_spring_project.demo.mapper.StudentMapper;
import com.tu.java_spring_project.demo.model.Role;
import com.tu.java_spring_project.demo.model.Student;
import com.tu.java_spring_project.demo.repository.StudentRepo;
import com.tu.java_spring_project.demo.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceTest {

    @InjectMocks
    private StudentService studentService;

    @Mock
    private StudentRepo studentRepo;

    @Mock
    private StudentMapper studentMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    private Student student;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        student = Student.builder()
                .id(1L)
                .facultyNumber("123456789")
                .email("mivanova@example.com")
                .enabled(false)
                .password("encodedOldPass")
                .build();
    }

    @Test
    void registerStudent_ShouldReturnSavedStudent() {
        StudentRegisterRequestDTO request = new StudentRegisterRequestDTO(
                "Maria", "Ivanova", "123456789", "mivanova@example.com", null
        );

        when(studentRepo.existsByFacultyNumber("123456789")).thenReturn(false);
        when(studentMapper.toStudent(request)).thenReturn(student);
        when(studentRepo.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Student saved = studentService.registerStudent(request);

        assertNotNull(saved);
        assertEquals(Role.STUDENT, saved.getRole());
        assertFalse(saved.isEnabled());
        assertNotNull(saved.getActivationToken());
        assertNull(saved.getPassword());

        verify(studentRepo, times(1)).save(student);
    }

    @Test
    void registerStudent_DuplicateFacultyNumber_ShouldThrow() {
        StudentRegisterRequestDTO request = new StudentRegisterRequestDTO(
                "Maria", "Ivanova", "123456789", "mivanova@example.com", null
        );

        when(studentRepo.existsByFacultyNumber("123456789")).thenReturn(true);

        DuplicateResourceException ex = assertThrows(DuplicateResourceException.class,
                () -> studentService.registerStudent(request));

        assertEquals("Student with facultyNumber=123456789 already exists", ex.getMessage());
        verify(studentRepo, never()).save(any());
    }

    @Test
    void changePasswordForStudent_ShouldUpdatePassword() {
        String oldPassword = "oldPass";
        String newPassword = "newPass";

        when(studentRepo.findStudentByFacultyNumber("123456789")).thenReturn(Optional.of(student));
        when(passwordEncoder.matches(oldPassword, "encodedOldPass")).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPass");
        when(studentRepo.save(student)).thenReturn(student);

        studentService.changePasswordForStudent("123456789", oldPassword, newPassword, passwordEncoder);

        verify(studentRepo, times(1)).save(student);
        assertEquals("encodedNewPass", student.getPassword());
    }

    @Test
    void changePasswordForStudent_StudentNotFound_ShouldThrow() {
        when(studentRepo.findStudentByFacultyNumber("999999999")).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> studentService.changePasswordForStudent("999999999", "oldPass", "newPass", passwordEncoder)
        );

        assertEquals("Student not found with faculty number: 999999999", ex.getMessage());
        verify(studentRepo, never()).save(any());
    }

    @Test
    void changePasswordForStudent_WrongOldPassword_ShouldThrow() {
        when(studentRepo.findStudentByFacultyNumber("123456789")).thenReturn(Optional.of(student));
        when(passwordEncoder.matches("wrongOldPass", "encodedOldPass")).thenReturn(false);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> studentService.changePasswordForStudent("123456789", "wrongOldPass", "newPass", passwordEncoder)
        );

        assertEquals("Old password is incorrect", ex.getMessage());
        verify(studentRepo, never()).save(any());
    }
}

