package com.tu.java_spring_project.demo.mapper;

import com.tu.java_spring_project.demo.dto.auth.student.StudentRegisterRequestDTO;
import com.tu.java_spring_project.demo.dto.auth.student.StudentRegisterResponseDTO;
import com.tu.java_spring_project.demo.dto.auth.teacher.TeacherRegisterRequestDTO;
import com.tu.java_spring_project.demo.dto.auth.teacher.TeacherRegisterResponseDTO;
import com.tu.java_spring_project.demo.dto.student.StudentRequestDto;
import com.tu.java_spring_project.demo.dto.student.StudentResponseDto;
import com.tu.java_spring_project.demo.model.Enrollment;
import com.tu.java_spring_project.demo.model.Student;
import com.tu.java_spring_project.demo.model.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;
import java.util.stream.Collectors;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface StudentMapper {


    @Mapping(target = "courseNames", source = "enrollments")
    StudentResponseDto toStudentResponseDto(Student student);

    Student toStudent(StudentRequestDto studentRequestDto);
    List<StudentResponseDto> toStudentResponseDtoList(List<Student> students);

    StudentRegisterResponseDTO toStudentRegisterResponseDTO(Student student);

    @Mapping(target = "academicYear", source = "academicYear")
    Student toStudent(StudentRegisterRequestDTO studentRegisterRequestDto);

    // Мапване на enrollment → courseName
    default List<String> mapEnrollmentsToCourseNames(List<Enrollment> enrollments) {
        if (enrollments == null) return null;
        return enrollments.stream()
                .map(enrollment -> enrollment.getCourse().getName())
                .collect(Collectors.toList());
    }
}
