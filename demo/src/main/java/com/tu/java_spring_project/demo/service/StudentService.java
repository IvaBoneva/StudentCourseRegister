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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentService {

    private final StudentRepo studentRepo;
    private final StudentMapper studentMapper;

    // Връща лист с всички студенти
    public List<StudentResponseDto> getAllStudents() {
        return studentMapper.toStudentResponseDtoList(studentRepo.findAll());
    }

    // Връща студент по id
    public StudentResponseDto getStudentByIdOrThrow(Long id) {
        Student student = studentRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Student not found with id " + id));
        return studentMapper.toStudentResponseDto(student);
    }

    // Създава и запазва студент
    @Transactional
    public StudentResponseDto saveStudent(StudentRequestDto dto) {
        Student student = studentMapper.toStudent(dto);
        student.setEnrollments(new ArrayList<>());
        Student saved = studentRepo.save(student);
        return studentMapper.toStudentResponseDto(saved);
    }

    // Редактира студент по id
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

    // Трие студент по id
    @Transactional
    public void deleteStudent(Long id) {
        if (!studentRepo.existsById(id)) {
            throw new NoSuchElementException("Student not found with id " + id);
        }
        studentRepo.deleteById(id);
    }

}
