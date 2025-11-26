package com.tu.java_spring_project.demo.mapper;

import com.tu.java_spring_project.demo.dto.TeacherRequestDto;
import com.tu.java_spring_project.demo.dto.TeacherResponseDto;
import com.tu.java_spring_project.demo.model.Course;
import com.tu.java_spring_project.demo.model.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface TeacherMapper {

    @Mapping(target = "courseNames", source = "courses", qualifiedByName = "mapCoursesToNames")
    TeacherResponseDto toTeacherResponseDto(Teacher teacher);

    Teacher toTeacher(TeacherRequestDto teacherRequestDto);
    List<TeacherResponseDto> toTeacherResponseDtoList(List<Teacher> teachers);

    @Named("mapCoursesToNames")
    default List<String> mapCoursesToCourseNames(List<Course> courses) {
        if (courses == null) return List.of();
        return courses.stream()
                .map(Course::getCourseName) // или getName() ако имаш такъв
                .toList();
    }
}
