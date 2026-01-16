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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceTest {

    @InjectMocks
    private StudentService studentService;

    @Mock
    private StudentRepo studentRepo;

    @Mock
    private StudentMapper studentMapper;

    private Student student;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        student = Student.builder()
                .id(1L)
                .facultyNumber("123456789")
                .email("mivanova@example.com")
                .enabled(false)
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
}

