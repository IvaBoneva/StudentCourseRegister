package com.tu.java_spring_project.demo.service;

import com.tu.java_spring_project.demo.dto.StudentRequestDto;
import com.tu.java_spring_project.demo.dto.StudentResponseDto;
import com.tu.java_spring_project.demo.dto.TeacherRequestDto;
import com.tu.java_spring_project.demo.dto.TeacherResponseDto;
import com.tu.java_spring_project.demo.mapper.TeacherMapper;
import com.tu.java_spring_project.demo.model.Student;
import com.tu.java_spring_project.demo.model.Teacher;
import com.tu.java_spring_project.demo.repository.TeacherRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
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
}