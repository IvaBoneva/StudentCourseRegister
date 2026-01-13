package com.tu.java_spring_project.demo.mapper;

import com.tu.java_spring_project.demo.dto.CourseRequestDto;
import com.tu.java_spring_project.demo.dto.CourseResponseDto;
import com.tu.java_spring_project.demo.model.Course;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-13T18:37:47+0200",
    comments = "version: 1.6.0, compiler: javac, environment: Java 17.0.16 (Eclipse Adoptium)"
)
@Component
public class CourseMapperImpl implements CourseMapper {

    @Override
    public CourseResponseDto toCourseResponseDto(Course course) {
        if ( course == null ) {
            return null;
        }

        Long roomId = null;
        List<String> studentNames = null;
        List<String> teacherNames = null;
        Long id = null;
        String courseName = null;
        int credits = 0;

        roomId = mapRoomToRoomId( course.getRoom() );
        studentNames = mapEnrollmentsToStudentNames( course.getEnrollments() );
        teacherNames = mapEnrollmentsToTeacherNames( course.getEnrollments() );
        id = course.getId();
        courseName = course.getCourseName();
        credits = course.getCredits();

        CourseResponseDto courseResponseDto = new CourseResponseDto( id, courseName, credits, roomId, studentNames, teacherNames );

        return courseResponseDto;
    }

    @Override
    public Course toCourse(CourseRequestDto courseRequestDto) {
        if ( courseRequestDto == null ) {
            return null;
        }

        Course course = new Course();

        course.setCourseName( courseRequestDto.courseName() );
        course.setCredits( courseRequestDto.credits() );

        return course;
    }

    @Override
    public List<CourseResponseDto> toCourseResponseDtoList(List<Course> courses) {
        if ( courses == null ) {
            return null;
        }

        List<CourseResponseDto> list = new ArrayList<CourseResponseDto>( courses.size() );
        for ( Course course : courses ) {
            list.add( toCourseResponseDto( course ) );
        }

        return list;
    }
}
