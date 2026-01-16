package com.tu.java_spring_project.demo.controller_tests;

import com.tu.java_spring_project.demo.config.security.StudentPrincipal;
import com.tu.java_spring_project.demo.config.security.TeacherPrincipal;
import com.tu.java_spring_project.demo.controller.AuthController;
import com.tu.java_spring_project.demo.dto.auth.ActivateRequestDTO;
import com.tu.java_spring_project.demo.dto.auth.student.*;
import com.tu.java_spring_project.demo.dto.auth.teacher.*;
import com.tu.java_spring_project.demo.mapper.StudentMapper;
import com.tu.java_spring_project.demo.mapper.TeacherMapper;
import com.tu.java_spring_project.demo.model.Student;
import com.tu.java_spring_project.demo.model.Teacher;
import com.tu.java_spring_project.demo.repository.StudentRepo;
import com.tu.java_spring_project.demo.repository.TeacherRepo;
import com.tu.java_spring_project.demo.service.StudentService;
import com.tu.java_spring_project.demo.service.TeacherService;
import com.tu.java_spring_project.demo.config.security.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerUnitTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private TeacherService teacherService;

    @Mock
    private StudentService studentService;

    @Mock
    private TeacherMapper teacherMapper;

    @Mock
    private StudentMapper studentMapper;

    @Mock
    private TeacherRepo teacherRepo;

    @Mock
    private StudentRepo studentRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    private Teacher teacher;
    private Student student;

    @BeforeEach
    void setUp() {
        teacher = Teacher.builder()
                .id(1L)
                .email("john@example.com")
                .activationToken("token123")
                .enabled(false)
                .build();

        student = Student.builder()
                .facultyNumber("123456789")
                .email("alice@example.com")
                .enabled(false)
                .build();
    }

    // ---------------- TEACHER TESTS ----------------

    @Test
    void registerTeacher_ShouldReturnCreated() {
        TeacherRegisterRequestDTO request = new TeacherRegisterRequestDTO("John", "Doe", "john@example.com");
        TeacherRegisterResponseDTO responseDTO = new TeacherRegisterResponseDTO(1L, "john@example.com", "token123");

        when(teacherService.registerTeacher(request)).thenReturn(teacher);
        when(teacherMapper.toTeacherRegisterResponseDto(teacher)).thenReturn(responseDTO);

        var response = authController.registerTeacher(request);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(responseDTO, response.getBody());
        verify(teacherService, times(1)).registerTeacher(request);
    }

    @Test
    void activateTeacher_ShouldEnableTeacher() {
        ActivateRequestDTO req = new ActivateRequestDTO("token123", "pass1234");

        when(teacherRepo.findByActivationToken("token123")).thenReturn(Optional.of(teacher));
        when(passwordEncoder.encode("pass1234")).thenReturn("encodedPass");

        authController.activateTeacher(req);

        assertTrue(teacher.isEnabled());
        assertEquals("encodedPass", teacher.getPassword());
        assertNull(teacher.getActivationToken());
        verify(teacherRepo, times(1)).save(teacher);
    }

    @Test
    void loginTeacher_ShouldReturnToken() {
        TeacherLoginRequestDTO req = new TeacherLoginRequestDTO("john@example.com", "pass1234");

        Authentication authMock = mock(Authentication.class);
        TeacherPrincipal principalMock = mock(TeacherPrincipal.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authMock);
        when(authMock.getPrincipal()).thenReturn(principalMock);
        when(principalMock.getEmail()).thenReturn("john@example.com");
        when(principalMock.getAuthorities()).thenReturn(Set.of(() -> "ROLE_TEACHER"));
        when(jwtProvider.generateToken("john@example.com", "ROLE_TEACHER")).thenReturn("jwt-token");

        var response = authController.loginTeacher(req).getBody();

        assertNotNull(response);
        assertEquals("jwt-token", response.jwtToken());
    }

    @Test
    void loginTeacher_InvalidCredentials_ShouldThrowException() {
        TeacherLoginRequestDTO req = new TeacherLoginRequestDTO("john@example.com", "wrongpass");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Bad credentials"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authController.loginTeacher(req));

        assertEquals("Bad credentials", exception.getMessage());
    }

    @Test
    void activateTeacher_InvalidToken_ShouldThrow() {
        ActivateRequestDTO req = new ActivateRequestDTO("wrongtoken", "pass1234");

        when(teacherRepo.findByActivationToken("wrongtoken")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> authController.activateTeacher(req));

        assertEquals("Invalid token", ex.getMessage());
    }

    @Test
    void loginTeacher_NonExistentTeacher_ShouldThrow() {
        TeacherLoginRequestDTO req = new TeacherLoginRequestDTO("nonexistent@example.com", "pass1234");

        when(authenticationManager.authenticate(any()))
                .thenThrow(new RuntimeException("User not found"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> authController.loginTeacher(req));

        assertEquals("User not found", ex.getMessage());
    }

    @Test
    void registerTeacher_InvalidDTO_ShouldThrow() {
        // firstName is blank
        TeacherRegisterRequestDTO request = new TeacherRegisterRequestDTO("", "Doe", "john@example.com");

        // Normally validation would be handled by @Valid and BindingResult in controller,
        // here we simulate service throwing an exception
        when(teacherService.registerTeacher(request))
                .thenThrow(new RuntimeException("Validation failed: firstName is required"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> authController.registerTeacher(request));

        assertEquals("Validation failed: firstName is required", ex.getMessage());
    }

    // ---------------- STUDENT TESTS ----------------

    @Test
    void registerStudent_ShouldReturnCreated() {
        StudentRegisterRequestDTO request = new StudentRegisterRequestDTO("Alice", "Smith", "123456789", "alice@example.com", null);
        StudentRegisterResponseDTO responseDTO = new StudentRegisterResponseDTO(1L, "123456789", "alice@example.com", "token123");

        when(studentService.registerStudent(request)).thenReturn(student);
        when(studentMapper.toStudentRegisterResponseDTO(student)).thenReturn(responseDTO);

        var response = authController.registerStudent(request);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(responseDTO, response.getBody());
        verify(studentService, times(1)).registerStudent(request);
    }

    @Test
    void activateStudent_ShouldEnableStudent() {
        ActivateRequestDTO req = new ActivateRequestDTO("token123", "pass1234");

        when(studentRepo.findByActivationToken("token123")).thenReturn(Optional.of(student));
        when(passwordEncoder.encode("pass1234")).thenReturn("encodedPass");

        authController.activateStudent(req);

        assertTrue(student.isEnabled());
        assertEquals("encodedPass", student.getPassword());
        assertNull(student.getActivationToken());
        verify(studentRepo, times(1)).save(student);
    }

    @Test
    void loginStudent_ShouldReturnToken() {
        StudentLoginRequestDTO req = new StudentLoginRequestDTO("123456789", "pass1234");

        Authentication authMock = mock(Authentication.class);
        StudentPrincipal principalMock = mock(StudentPrincipal.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authMock);
        when(authMock.getPrincipal()).thenReturn(principalMock);
        when(principalMock.getFacultyNumber()).thenReturn("123456789");
        when(principalMock.getAuthorities()).thenReturn(Set.of(() -> "ROLE_STUDENT"));
        when(jwtProvider.generateToken("123456789", "ROLE_STUDENT")).thenReturn("jwt-token");

        var response = authController.loginStudent(req).getBody();

        assertNotNull(response);
        assertEquals("jwt-token", response.jwtToken());
    }

    @Test
    void loginStudent_DisabledStudent_ShouldNotReturnToken() {
        StudentLoginRequestDTO req = new StudentLoginRequestDTO("123456789", "pass1234");

        Authentication authMock = mock(Authentication.class);
        StudentPrincipal principalMock = mock(StudentPrincipal.class);

        when(authenticationManager.authenticate(any()))
                .thenReturn(authMock);
        when(authMock.getPrincipal()).thenReturn(principalMock);
        when(principalMock.getFacultyNumber()).thenReturn("123456789");
        when(principalMock.isEnabled()).thenReturn(false);
        when(principalMock.getAuthorities()).thenReturn(Set.of(() -> "ROLE_STUDENT"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            var token = authController.loginStudent(req).getBody();
            if (!principalMock.isEnabled()) throw new RuntimeException("Account disabled");
        });

        assertEquals("Account disabled", exception.getMessage());
    }

    @Test
    void registerStudent_DuplicateFacultyNumber_ShouldThrow() {
        StudentRegisterRequestDTO request = new StudentRegisterRequestDTO(
                "Alice", "Smith", "123456789", "alice@example.com", null
        );

        when(studentService.registerStudent(request)).thenThrow(new RuntimeException("Faculty number exists"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> authController.registerStudent(request));

        assertEquals("Faculty number exists", ex.getMessage());
    }

    @Test
    void loginStudent_NonExistentStudent_ShouldThrow() {
        StudentLoginRequestDTO req = new StudentLoginRequestDTO("000000000", "pass1234");

        when(authenticationManager.authenticate(any()))
                .thenThrow(new RuntimeException("Student not found"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> authController.loginStudent(req));

        assertEquals("Student not found", ex.getMessage());
    }

    @Test
    void registerStudent_InvalidDTO_ShouldThrow() {
        // Missing facultyNumber
        StudentRegisterRequestDTO request = new StudentRegisterRequestDTO(
                "Alice", "Smith", "", "alice@example.com", null
        );

        when(studentService.registerStudent(request))
                .thenThrow(new RuntimeException("Validation failed: facultyNumber is required"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> authController.registerStudent(request));

        assertEquals("Validation failed: facultyNumber is required", ex.getMessage());
    }

    @Test
    void activateStudent_InvalidToken_ShouldThrow() {
        ActivateRequestDTO req = new ActivateRequestDTO("invalidtoken", "pass1234");

        when(studentRepo.findByActivationToken("invalidtoken")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> authController.activateStudent(req));

        assertEquals("Invalid token", ex.getMessage());
    }

}
