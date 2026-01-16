package com.tu.java_spring_project.demo.mapper;

import com.tu.java_spring_project.demo.dto.enrollment.EnrollmentRequestDto;
import com.tu.java_spring_project.demo.dto.enrollment.EnrollmentResponseDto;
import com.tu.java_spring_project.demo.model.Enrollment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EnrollmentMapper {

    @Mapping(target = "studentName", expression = "java(formatStudentName(enrollment))")
    @Mapping(target = "teacherName", expression = "java(formatTeacherName(enrollment))")
    @Mapping(target = "courseName", source = "course.courseName")
    @Mapping(target = "gradeValue", source = "gradeValue")
    @Mapping(target = "gradedAt", source = "gradedAt")
    EnrollmentResponseDto toDto(Enrollment enrollment);

    Enrollment toEntity(EnrollmentRequestDto dto);

    default String formatStudentName(Enrollment e) {
        return e.getStudent().getFirstName() + " " + e.getStudent().getLastName();
    }

    default String formatTeacherName(Enrollment e) {
        return e.getTeacher().getFirstName() + " " + e.getTeacher().getLastName();
    }

}