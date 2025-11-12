package com.tu.java_spring_project.demo.mapper;

import com.tu.java_spring_project.demo.dto.StudentResponseDto;
import com.tu.java_spring_project.demo.dto.TeacherRequestDto;
import com.tu.java_spring_project.demo.dto.TeacherResponseDto;
import com.tu.java_spring_project.demo.model.Course;
import com.tu.java_spring_project.demo.model.Student;
import com.tu.java_spring_project.demo.model.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TeacherMapper {

    @Mapping(target = "courseNames", source = "courses")
    TeacherResponseDto toTeacherResponseDto(Teacher teacher);

    Teacher toTeacher(TeacherRequestDto teacherRequestDto);
    List<TeacherResponseDto> toTeacherResponseDtoList(List<Teacher> teachers);

    // Мапване на course → courseName
    default List<String> mapCoursesToCourseNames(List<Course> courses) {
        if (courses == null) return null;
        return courses.stream()
                .map(course -> course.getName())
                .collect(Collectors.toList());
    }


}
