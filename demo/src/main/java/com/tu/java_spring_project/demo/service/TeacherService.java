package com.tu.java_spring_project.demo.service;

import com.tu.java_spring_project.demo.config.security.TeacherPrincipal;
import com.tu.java_spring_project.demo.dto.auth.teacher.TeacherRegisterRequestDTO;
import com.tu.java_spring_project.demo.dto.teacher.TeacherRequestDto;
import com.tu.java_spring_project.demo.dto.teacher.TeacherResponseDto;
import com.tu.java_spring_project.demo.exception.DuplicateResourceException;
import com.tu.java_spring_project.demo.mapper.TeacherMapper;
import com.tu.java_spring_project.demo.model.Role;
import com.tu.java_spring_project.demo.model.Teacher;
import com.tu.java_spring_project.demo.repository.TeacherRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class TeacherService {

    private final TeacherRepo teacherRepo;
    private final TeacherMapper teacherMapper;

    // Връща лист с всички учители
    public List<TeacherResponseDto> getAllTeachers() {
        return teacherMapper.toTeacherResponseDtoList(teacherRepo.findAll());
    }

    // Връща учител по id
    public TeacherResponseDto getTeacherByIdOrThrow(Long id) {
        Teacher teacher = teacherRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Teacher not found with id " + id));
        return teacherMapper.toTeacherResponseDto(teacher);
    }

    // Създава и запазва учител
    @Transactional
    public TeacherResponseDto saveTeacher(TeacherRequestDto dto) {
        Teacher teacher = teacherMapper.toTeacher(dto);
        teacher.setEnrollments(new ArrayList<>()); // празен списък от enrollments
        Teacher saved = teacherRepo.save(teacher);
        return teacherMapper.toTeacherResponseDto(saved);
    }


    // Редактира учител по id
    @Transactional
    public TeacherResponseDto updateTeacher(Long id, TeacherRequestDto dto) {
        Teacher existing = teacherRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Teacher not found with id " + id));

        existing.setFirstName(dto.firstName());
        existing.setLastName(dto.lastName());
        existing.setEmail(dto.email());

        Teacher updated = teacherRepo.save(existing);
        return teacherMapper.toTeacherResponseDto(updated);
    }

    // Трие учител по id
    @Transactional
    public void deleteTeacher(Long id) {
        if (!teacherRepo.existsById(id)) {
            throw new NoSuchElementException("Teacher not found with id " + id);
        }
        teacherRepo.deleteById(id);
    }

    @Transactional
    public Teacher registerTeacher(TeacherRegisterRequestDTO registerRequestDTO) {

        if (teacherRepo.existsByEmail(registerRequestDTO.email())) {
            throw new DuplicateResourceException(
                    Teacher.class, "email", registerRequestDTO.email()
            );
        }

        Teacher teacher = teacherMapper.toTeacher(registerRequestDTO);

        teacher.setRole(Role.TEACHER);
        teacher.setEnabled(false);
        teacher.setActivationToken(UUID.randomUUID().toString());
        teacher.setPassword(null);

        return teacherRepo.save(teacher);
    }

    @Transactional
    public Teacher patchTeacherRole(Long teacherId, Role newRole) {

        Teacher teacher = teacherRepo.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        TeacherPrincipal principal = (TeacherPrincipal) auth.getPrincipal();
        Long currentUserId = principal.getTeacherId();

        if (teacher.getId().equals(currentUserId)) {
            throw new IllegalStateException("You cannot change your own role");
        }

        teacher.setRole(newRole);
        return teacherRepo.save(teacher);
    }
}