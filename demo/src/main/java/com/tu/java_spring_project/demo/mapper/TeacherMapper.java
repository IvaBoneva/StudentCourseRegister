package com.tu.java_spring_project.demo.mapper;

import com.tu.java_spring_project.demo.dto.auth.teacher.TeacherRegisterRequestDTO;
import com.tu.java_spring_project.demo.dto.auth.teacher.TeacherRegisterResponseDTO;
import com.tu.java_spring_project.demo.dto.teacher.TeacherRequestDto;
import com.tu.java_spring_project.demo.dto.teacher.TeacherResponseDto;
import com.tu.java_spring_project.demo.model.Enrollment;
import com.tu.java_spring_project.demo.model.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

import org.mapstruct.Named;
@Mapper(componentModel = "spring")
public interface TeacherMapper {

    @Mapping(target = "courseNames", source = "enrollments", qualifiedByName = "mapEnrollmentsToCourseNames")
    TeacherResponseDto toTeacherResponseDto(Teacher teacher);

    TeacherRegisterResponseDTO toTeacherRegisterResponseDto(Teacher teacher);

    Teacher toTeacher(TeacherRequestDto teacherRequestDto);
    Teacher toTeacher(TeacherRegisterRequestDTO teacherRegisterRequestDto);
    List<TeacherResponseDto> toTeacherResponseDtoList(List<Teacher> teachers);

    @Named("mapEnrollmentsToCourseNames")
    default List<String> mapEnrollmentsToCourseNames(List<Enrollment> enrollments) {
        if (enrollments == null) return List.of();
        return enrollments.stream()
                .map(enrollment -> enrollment.getCourse().getCourseName())
                .distinct() // премахва дублирани курсове
                .toList();
    }
}
