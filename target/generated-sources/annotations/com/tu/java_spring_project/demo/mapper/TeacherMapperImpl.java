package com.tu.java_spring_project.demo.mapper;

import com.tu.java_spring_project.demo.dto.TeacherRequestDto;
import com.tu.java_spring_project.demo.dto.TeacherResponseDto;
import com.tu.java_spring_project.demo.model.Teacher;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-26T20:44:30+0200",
    comments = "version: 1.6.0, compiler: javac, environment: Java 17.0.16 (Eclipse Adoptium)"
)
@Component
public class TeacherMapperImpl implements TeacherMapper {

    @Override
    public TeacherResponseDto toTeacherResponseDto(Teacher teacher) {
        if ( teacher == null ) {
            return null;
        }

        List<String> courseNames = null;
        Long id = null;
        String firstName = null;
        String lastName = null;
        String email = null;

        courseNames = mapCoursesToCourseNames( teacher.getCourses() );
        id = teacher.getId();
        firstName = teacher.getFirstName();
        lastName = teacher.getLastName();
        email = teacher.getEmail();

        TeacherResponseDto teacherResponseDto = new TeacherResponseDto( id, firstName, lastName, email, courseNames );

        return teacherResponseDto;
    }

    @Override
    public Teacher toTeacher(TeacherRequestDto teacherRequestDto) {
        if ( teacherRequestDto == null ) {
            return null;
        }

        Teacher teacher = new Teacher();

        teacher.setFirstName( teacherRequestDto.firstName() );
        teacher.setLastName( teacherRequestDto.lastName() );
        teacher.setEmail( teacherRequestDto.email() );

        return teacher;
    }

    @Override
    public List<TeacherResponseDto> toTeacherResponseDtoList(List<Teacher> teachers) {
        if ( teachers == null ) {
            return null;
        }

        List<TeacherResponseDto> list = new ArrayList<TeacherResponseDto>( teachers.size() );
        for ( Teacher teacher : teachers ) {
            list.add( toTeacherResponseDto( teacher ) );
        }

        return list;
    }
}
