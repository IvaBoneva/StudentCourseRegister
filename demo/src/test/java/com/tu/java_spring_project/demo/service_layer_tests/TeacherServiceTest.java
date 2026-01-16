package com.tu.java_spring_project.demo.service_layer_tests;

import com.tu.java_spring_project.demo.dto.auth.teacher.TeacherRegisterRequestDTO;
import com.tu.java_spring_project.demo.mapper.TeacherMapper;
import com.tu.java_spring_project.demo.model.Teacher;
import com.tu.java_spring_project.demo.model.Role;
import com.tu.java_spring_project.demo.repository.TeacherRepo;
import com.tu.java_spring_project.demo.service.TeacherService;
import com.tu.java_spring_project.demo.config.security.TeacherPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TeacherServiceTest {

    @InjectMocks
    private TeacherService teacherService;

    @Mock
    private TeacherRepo teacherRepo;

    @Mock
    private TeacherMapper teacherMapper;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    private Teacher teacher;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        teacher = Teacher.builder()
                .id(1L)
                .email("tpetrov@example.com")
                .role(Role.TEACHER)
                .enabled(false)
                .activationToken("token123")
                .build();
    }

    @Test
    void registerTeacher_ShouldReturnSavedTeacher() {
        TeacherRegisterRequestDTO request = new TeacherRegisterRequestDTO("Todor", "Petrov", "tpetrov@example.com");

        when(teacherRepo.existsByEmail("tpetrov@example.com")).thenReturn(false);
        when(teacherMapper.toTeacher(request)).thenReturn(teacher);
        when(teacherRepo.save(any(Teacher.class))).thenReturn(teacher);

        Teacher saved = teacherService.registerTeacher(request);

        assertNotNull(saved);
        assertEquals(Role.TEACHER, saved.getRole());
        assertFalse(saved.isEnabled());
        assertNotNull(saved.getActivationToken());
        assertNull(saved.getPassword());

        verify(teacherRepo, times(1)).save(teacher);
    }

    @Test
    void registerTeacher_DuplicateEmail_ShouldThrow() {
        TeacherRegisterRequestDTO request = new TeacherRegisterRequestDTO("Todor", "Petrov", "tpetrov@example.com");

        when(teacherRepo.existsByEmail("tpetrov@example.com")).thenReturn(true);

        assertThrows(
                com.tu.java_spring_project.demo.exception.DuplicateResourceException.class,
                () -> teacherService.registerTeacher(request)
        );

        verify(teacherRepo, never()).save(any());
    }

    @Test
    void patchTeacherRole_ShouldChangeRole() {
        Teacher targetTeacher = Teacher.builder().id(2L).role(Role.TEACHER).build();

        when(teacherRepo.findById(2L)).thenReturn(Optional.of(targetTeacher));

        TeacherPrincipal currentPrincipal = mock(TeacherPrincipal.class);
        when(currentPrincipal.getTeacherId()).thenReturn(1L); // current user ID

        when(authentication.getPrincipal()).thenReturn(currentPrincipal);
        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(context);

        when(teacherRepo.save(targetTeacher)).thenReturn(targetTeacher);

        Teacher updated = teacherService.patchTeacherRole(2L, Role.ADMIN);

        assertEquals(Role.ADMIN, updated.getRole());
        verify(teacherRepo, times(1)).save(targetTeacher);
    }

    @Test
    void patchTeacherRole_CannotChangeOwnRole_ShouldThrow() {
        Teacher targetTeacher = Teacher.builder().id(1L).role(Role.TEACHER).build();

        when(teacherRepo.findById(1L)).thenReturn(Optional.of(targetTeacher));

        TeacherPrincipal currentPrincipal = mock(TeacherPrincipal.class);
        when(currentPrincipal.getTeacherId()).thenReturn(1L); // same user

        when(authentication.getPrincipal()).thenReturn(currentPrincipal);
        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(context);

        assertThrows(IllegalStateException.class,
                () -> teacherService.patchTeacherRole(1L, Role.ADMIN));

        verify(teacherRepo, never()).save(any());
    }

    @Test
    void patchTeacherRole_TeacherNotFound_ShouldThrow() {
        when(teacherRepo.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> teacherService.patchTeacherRole(999L, Role.ADMIN));
    }

    @Test
    void changePasswordForTeacher_ShouldUpdatePassword() {
        String email = "tpetrov@example.com";
        String oldPassword = "oldPass";
        String newPassword = "newPass";

        teacher.setPassword("encodedOldPass"); // current password in DB

        when(teacherRepo.findTeacherByEmail(email)).thenReturn(Optional.of(teacher));
        when(passwordEncoder.matches(oldPassword, "encodedOldPass")).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPass");
        when(teacherRepo.save(any(Teacher.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        teacherService.changePasswordForTeacher(email, oldPassword, newPassword, passwordEncoder);

        // Assert
        assertEquals("encodedNewPass", teacher.getPassword());
        verify(teacherRepo, times(1)).save(teacher);
    }

    @Test
    void changePasswordForTeacher_InvalidOldPassword_ShouldThrow() {
        String email = "tpetrov@example.com";
        String oldPassword = "wrongOld";
        String newPassword = "newPass";

        teacher.setPassword("encodedOldPass");
        when(teacherRepo.findTeacherByEmail(email)).thenReturn(Optional.of(teacher));
        when(passwordEncoder.matches(oldPassword, "encodedOldPass")).thenReturn(false);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                teacherService.changePasswordForTeacher(email, oldPassword, newPassword, passwordEncoder));

        assertEquals("Old password is incorrect", ex.getMessage());
        verify(teacherRepo, never()).save(any());
    }

    @Test
    void changePasswordForTeacher_TeacherNotFound_ShouldThrow() {
        String email = "missing@example.com";
        when(teacherRepo.findTeacherByEmail(email)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                teacherService.changePasswordForTeacher(email, "oldPass", "newPass", passwordEncoder));

        assertEquals("Teacher not found with email: " + email, ex.getMessage());
        verify(teacherRepo, never()).save(any());
    }

}

