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

    @Mapping(target = "enrollmentIds", source = "enrollments")
    @Mapping(target = "courseNames", source = "enrollments")
    StudentResponseDto toStudentResponseDto(Student student);
    Student toStudent(StudentRequestDto studentRequestDto);


    // Спомагателни методи – MapStruct ги извиква автоматично
    // Мапване на enrollment → enrollmentId
    default List<Long> mapEnrollmentsToIds(List<Enrollment> enrollments) {
        if (enrollments == null) return null;
        return enrollments.stream()
                .map(Enrollment::getId)
                .collect(Collectors.toList());
    }

    // Мапване на enrollment → courseName
    default List<String> mapEnrollmentsToCourseNames(List<Enrollment> enrollments) {
        if (enrollments == null) return null;
        return enrollments.stream()
                .map(enrollment -> enrollment.getCourse().getName())
                .collect(Collectors.toList());
    }
}
