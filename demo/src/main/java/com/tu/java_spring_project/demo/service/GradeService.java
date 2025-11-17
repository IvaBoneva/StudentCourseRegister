package com.tu.java_spring_project.demo.service;

import com.tu.java_spring_project.demo.dto.GradeRequestDto;
import com.tu.java_spring_project.demo.dto.GradeResponseDto;
import com.tu.java_spring_project.demo.mapper.GradeMapper;
import com.tu.java_spring_project.demo.model.Grade;
import com.tu.java_spring_project.demo.model.Room;
import com.tu.java_spring_project.demo.model.Student;
import com.tu.java_spring_project.demo.repository.GradeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GradeService {

    private final GradeRepo gradeRepo;
    private final GradeMapper gradeMapper;

    // Връща лист с всички оценки
    public List<GradeResponseDto> getAllGrades() {
        return gradeMapper.toGradeResponseDtoList(gradeRepo.findAll());
    }

    // Връща оценка по id
    public GradeResponseDto getGradeByIdOrThrow(Long id) {
        Grade grade = gradeRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Grade not found with id " + id));
        return gradeMapper.toGradeResponseDto(grade);
    }

    // Създава и запазва оценка
    @Transactional
    public GradeResponseDto saveGrade(GradeRequestDto dto) {
        Grade grade = gradeMapper.toGrade(dto);
        grade.setEnrollment(null);
        Grade saved = gradeRepo.save(grade);
        return gradeMapper.toGradeResponseDto(saved);
    }

    // Редактира оценка по id
    @Transactional
    public GradeResponseDto updateGrade(Long id, GradeRequestDto dto) {
        Grade existing = gradeRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Grade not found with id " + id));

        existing.setGradeValue(dto.gradeValue());
        existing.setGradedAt(dto.gradedAt());

        Grade updated = gradeRepo.save(existing);
        return gradeMapper.toGradeResponseDto(updated);
    }

    // Трие оценка по id
    @Transactional
    public void deleteGrade(Long id) {
        if (!gradeRepo.existsById(id)) {
            throw new NoSuchElementException("Grade not found with id " + id);
        }
        gradeRepo.deleteById(id);
    }
}
