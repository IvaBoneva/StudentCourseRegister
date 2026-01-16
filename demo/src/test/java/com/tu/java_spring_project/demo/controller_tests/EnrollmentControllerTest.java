package com.tu.java_spring_project.demo.controller_tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tu.java_spring_project.demo.config.security.JwtAuthFilter;
import com.tu.java_spring_project.demo.config.security.JwtProvider;
import com.tu.java_spring_project.demo.controller.EnrollmentController;
import com.tu.java_spring_project.demo.dto.enrollment.EnrollmentGradeUpdateDto;
import com.tu.java_spring_project.demo.dto.enrollment.EnrollmentRequestDto;
import com.tu.java_spring_project.demo.dto.enrollment.EnrollmentResponseDto;
import com.tu.java_spring_project.demo.service.EnrollmentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(EnrollmentController.class)
class EnrollmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EnrollmentService enrollmentService;

    @MockitoBean
    private JwtProvider jwtProvider;


    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;


    @Autowired
    private ObjectMapper objectMapper;

    // ================= createEnrollment =================
    @Test
    @WithMockUser(username="md@example.com", roles={"ADMIN"})
    void createEnrollment_returnsCreatedDto() throws Exception {
        EnrollmentRequestDto requestDto = new EnrollmentRequestDto(
                "901234577",      // studentFacultyNumber
                "Python Programming",     // courseName
                1L,             // teacherId
                5.5,            // gradeValue
                LocalDate.of(2026, 1, 11) // gradedAt
        );

        EnrollmentResponseDto responseDto = new EnrollmentResponseDto(
                1L,             // id
                "Sara Ivanova",     // studentName
                "Python Programming",     // courseName
                "Georgi Georgiev",    // teacherName
                5.5,
                LocalDate.of(2026, 1, 11)
        );

        Mockito.when(enrollmentService.createEnrollment(requestDto)).thenReturn(responseDto);

        mockMvc.perform(post("/api/enrollments/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(responseDto.id()))
                .andExpect(jsonPath("$.studentName").value(responseDto.studentName()))
                .andExpect(jsonPath("$.courseName").value(responseDto.courseName()))
                .andExpect(jsonPath("$.teacherName").value(responseDto.teacherName()))
                .andExpect(jsonPath("$.gradeValue").value(responseDto.gradeValue()))
                .andExpect(jsonPath("$.gradedAt").value(responseDto.gradedAt().toString()));
    }

    // ================= getAllEnrollments =================
    @Test
    void getAllEnrollments_returnsList() throws Exception {
        EnrollmentResponseDto enrollment1 = new EnrollmentResponseDto(
                1L, "Bilqna Hristova", "Math - advanced", "Kevin Stefanov", 5.0, LocalDate.of(2026, 1, 10));
        EnrollmentResponseDto enrollment2 = new EnrollmentResponseDto(
                2L, "Janet Filipova", "English lessons", "Kate Berry", 4.5, LocalDate.of(2026, 1, 12));
        List<EnrollmentResponseDto> responseList = List.of(enrollment1, enrollment2);

        Mockito.when(enrollmentService.getAllEnrollments()).thenReturn(responseList);

        mockMvc.perform(get("/api/enrollments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(enrollment1.id()))
                .andExpect(jsonPath("$[0].studentName").value(enrollment1.studentName()))
                .andExpect(jsonPath("$[0].courseName").value(enrollment1.courseName()))
                .andExpect(jsonPath("$[0].teacherName").value(enrollment1.teacherName()))
                .andExpect(jsonPath("$[0].gradeValue").value(enrollment1.gradeValue()))
                .andExpect(jsonPath("$[0].gradedAt").value(enrollment1.gradedAt().toString()))
                .andExpect(jsonPath("$[1].id").value(enrollment2.id()))
                .andExpect(jsonPath("$[1].studentName").value(enrollment2.studentName()))
                .andExpect(jsonPath("$[1].courseName").value(enrollment2.courseName()))
                .andExpect(jsonPath("$[1].teacherName").value(enrollment2.teacherName()))
                .andExpect(jsonPath("$[1].gradeValue").value(enrollment2.gradeValue()))
                .andExpect(jsonPath("$[1].gradedAt").value(enrollment2.gradedAt().toString()));
    }

    // ================= getEnrollmentById =================
    @Test
    void getEnrollmentById_returnsDto() throws Exception {
        Long id = 1L;
        EnrollmentResponseDto responseDto = new EnrollmentResponseDto(
                id, "Reneta Ismailova", "Sport", "Selina Karateneva", 5.0, LocalDate.of(2026, 1, 9));

        Mockito.when(enrollmentService.getEnrollmentById(id)).thenReturn(responseDto);

        mockMvc.perform(get("/api/enrollments/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseDto.id()))
                .andExpect(jsonPath("$.studentName").value(responseDto.studentName()))
                .andExpect(jsonPath("$.courseName").value(responseDto.courseName()))
                .andExpect(jsonPath("$.teacherName").value(responseDto.teacherName()))
                .andExpect(jsonPath("$.gradeValue").value(responseDto.gradeValue()))
                .andExpect(jsonPath("$.gradedAt").value(responseDto.gradedAt().toString()));
    }

    // ================= deleteEnrollment =================
    @Test
    void deleteEnrollment_returnsNoContent() throws Exception {
        Long id = 1L;

        Mockito.doNothing().when(enrollmentService).deleteEnrollment(id);

        mockMvc.perform(delete("/api/enrollments/{id}", id))
                .andExpect(status().isNoContent());
    }

    // ================= updateGrade =================
    @Test
    void updateGrade_returnsUpdatedDto() throws Exception {
        Long id = 1L;
        EnrollmentGradeUpdateDto updateDto = new EnrollmentGradeUpdateDto(6.0, LocalDate.of(2026, 1, 14));
        EnrollmentResponseDto updatedResponse = new EnrollmentResponseDto(
                id, "Ivelin Petrov", "Java Programming", "Kalina Aneva", 6.0, LocalDate.of(2026, 1, 14));

        Mockito.when(enrollmentService.updateGrade(id, updateDto)).thenReturn(updatedResponse);

        mockMvc.perform(put("/api/enrollments/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedResponse.id()))
                .andExpect(jsonPath("$.gradeValue").value(updatedResponse.gradeValue()));
    }
}
