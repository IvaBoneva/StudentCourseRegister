package com.tu.java_spring_project.demo.mapper;

import com.tu.java_spring_project.demo.dto.course.CourseRequestDto;
import com.tu.java_spring_project.demo.dto.course.CourseResponseDto;
import com.tu.java_spring_project.demo.model.Course;
import com.tu.java_spring_project.demo.model.Enrollment;
import com.tu.java_spring_project.demo.model.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CourseMapper {

    @Mapping(target = "roomId", source = "room")
    @Mapping(target = "studentNames", source = "enrollments", qualifiedByName = "enrollmentsToStudentNames")
    @Mapping(target = "teacherNames", source = "enrollments", qualifiedByName = "enrollmentsToTeacherNames")
    CourseResponseDto toCourseResponseDto(Course course);

    Course toCourse(CourseRequestDto courseRequestDto);
    List<CourseResponseDto> toCourseResponseDtoList(List<Course> courses);


    // Мапване на room → roomId
    default Long mapRoomToRoomId(Room room) {
        if (room == null) return null;
        return room.getId();
    }

    // Мапване на enrollments → studentNames
    @Named("enrollmentsToStudentNames")
    default List<String> mapEnrollmentsToStudentNames(List<Enrollment> enrollments) {
        if (enrollments == null) return null;
        return enrollments.stream()
                .map(enrollment -> enrollment.getStudent().getFirstName()
                        + " "
                        + enrollment.getStudent().getLastName())
                .collect(Collectors.toList());
    }

    // Мапване на enrollments → teacherNames
    @Named("enrollmentsToTeacherNames")
    default List<String> mapEnrollmentsToTeacherNames(List<Enrollment> enrollments) {
        if (enrollments == null) return null;
        return enrollments.stream()
                .map(enrollment -> enrollment.getTeacher().getFirstName()
                        + " "
                        + enrollment.getTeacher().getLastName())
                .collect(Collectors.toList());
    }


}