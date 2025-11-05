package com.tu.java_spring_project.demo.service;

import com.tu.java_spring_project.demo.dto.StudentRequestDto;
import com.tu.java_spring_project.demo.dto.StudentResponseDto;
import com.tu.java_spring_project.demo.mapper.StudentMapper;
import com.tu.java_spring_project.demo.model.Student;
import com.tu.java_spring_project.demo.repository.StudentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentService {

    private final StudentRepo studentRepo;
    private final StudentMapper studentMapper;

    public List<StudentResponseDto> getAllStudents() {
        return studentRepo.findAll()
                .stream()
                .map(studentMapper::toStudentResponseDto) // MapStruct мапва entity → DTO
                .collect(Collectors.toList());
    }

    // Връща студент по id
    public StudentResponseDto getStudentByIdOrThrow(Long id) {
        Student student = studentRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Student not found with id " + id));
        return studentMapper.toStudentResponseDto(student);
    }

    @Transactional
    public StudentResponseDto saveStudent(StudentRequestDto dto) {
        Student student = studentMapper.toStudent(dto);
        student.setEnrollments(new ArrayList<>());
        Student saved = studentRepo.save(student);
        return studentMapper.toStudentResponseDto(saved);
    }

    @Transactional
    public StudentResponseDto updateStudent(Long id, StudentRequestDto dto) {
        Student existing = studentRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Student not found with id " + id));

        existing.setFirstName(dto.firstName());
        existing.setLastName(dto.lastName());
        existing.setFacultyNumber(dto.facultyNumber());
        existing.setEmail(dto.email());
        existing.setAcademicYear(dto.academicYear());

        Student updated = studentRepo.save(existing);
        return studentMapper.toStudentResponseDto(updated);
    }

    @Transactional
    public void deleteStudent(Long id) {
        if (!studentRepo.existsById(id)) {
            throw new NoSuchElementException("Student not found with id " + id);
        }
        studentRepo.deleteById(id);
    }

}
