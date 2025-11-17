package com.tu.java_spring_project.demo.mapper;

import com.tu.java_spring_project.demo.dto.GradeRequestDto;
import com.tu.java_spring_project.demo.dto.GradeResponseDto;
import com.tu.java_spring_project.demo.model.Enrollment;
import com.tu.java_spring_project.demo.model.Grade;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface GradeMapper {

    @Mapping(target = "studentName", source = "enrollment", qualifiedByName = "enrollmentToStudentName")
    @Mapping(target = "courseName", source = "enrollment", qualifiedByName = "enrollmentToCourseName")
    GradeResponseDto toGradeResponseDto(Grade grade);

    Grade toGrade(GradeRequestDto gradeRequestDto);
    List<GradeResponseDto> toGradeResponseDtoList(List<Grade> grades);

    // Мапване на enrollment → studentName
    @Named("enrollmentToStudentName")
    default String mapEnrollmentToStudentName(Enrollment enrollment) {
        if (enrollment == null) return null;
        return enrollment.getStudent().getFirstName()
                        + " "
                        + enrollment.getStudent().getLastName();
    }

    // Мапване на enrollment → courseName
    @Named("enrollmentToCourseName")
    default String mapEnrollmentToCourseName(Enrollment enrollment) {
        if (enrollment == null) return null;
        return enrollment.getCourse().getName();
    }
}
