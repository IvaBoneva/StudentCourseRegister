package com.tu.java_spring_project.demo.mapper;

import com.tu.java_spring_project.demo.dto.GradeRequestDto;
import com.tu.java_spring_project.demo.dto.GradeResponseDto;
import com.tu.java_spring_project.demo.model.Grade;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-17T23:56:20+0200",
    comments = "version: 1.6.0, compiler: javac, environment: Java 17.0.16 (Eclipse Adoptium)"
)
@Component
public class GradeMapperImpl implements GradeMapper {

    @Override
    public GradeResponseDto toGradeResponseDto(Grade grade) {
        if ( grade == null ) {
            return null;
        }

        String studentName = null;
        String courseName = null;
        Long id = null;
        Double gradeValue = null;
        LocalDate gradedAt = null;

        studentName = mapEnrollmentToStudentName( grade.getEnrollment() );
        courseName = mapEnrollmentToCourseName( grade.getEnrollment() );
        id = grade.getId();
        gradeValue = grade.getGradeValue();
        gradedAt = grade.getGradedAt();

        GradeResponseDto gradeResponseDto = new GradeResponseDto( id, gradeValue, gradedAt, studentName, courseName );

        return gradeResponseDto;
    }

    @Override
    public Grade toGrade(GradeRequestDto gradeRequestDto) {
        if ( gradeRequestDto == null ) {
            return null;
        }

        Grade grade = new Grade();

        grade.setGradeValue( gradeRequestDto.gradeValue() );
        grade.setGradedAt( gradeRequestDto.gradedAt() );

        return grade;
    }

    @Override
    public List<GradeResponseDto> toGradeResponseDtoList(List<Grade> grades) {
        if ( grades == null ) {
            return null;
        }

        List<GradeResponseDto> list = new ArrayList<GradeResponseDto>( grades.size() );
        for ( Grade grade : grades ) {
            list.add( toGradeResponseDto( grade ) );
        }

        return list;
    }
}
