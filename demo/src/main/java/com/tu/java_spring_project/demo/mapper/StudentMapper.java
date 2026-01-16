package com.tu.java_spring_project.demo.mapper;

import com.tu.java_spring_project.demo.dto.StudentRequestDto;
import com.tu.java_spring_project.demo.dto.StudentResponseDto;
import com.tu.java_spring_project.demo.model.Enrollment;
import com.tu.java_spring_project.demo.model.Student;
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


    // Мапване на enrollment → courseName
    default List<String> mapEnrollmentsToCourseNames(List<Enrollment> enrollments) {
        if (enrollments == null) return null;
        return enrollments.stream()
                .map(enrollment -> enrollment.getCourse().getName())
                .distinct()
                .collect(Collectors.toList());
    }
}
