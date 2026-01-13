package com.tu.java_spring_project.demo.mapper;

import com.tu.java_spring_project.demo.dto.EnrollmentRequestDto;
import com.tu.java_spring_project.demo.dto.EnrollmentResponseDto;
import com.tu.java_spring_project.demo.model.Course;
import com.tu.java_spring_project.demo.model.Enrollment;
import java.time.LocalDate;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-13T18:56:38+0200",
    comments = "version: 1.6.0, compiler: javac, environment: Java 17.0.16 (Eclipse Adoptium)"
)
@Component
public class EnrollmentMapperImpl implements EnrollmentMapper {

    @Override
    public EnrollmentResponseDto toDto(Enrollment enrollment) {
        if ( enrollment == null ) {
            return null;
        }

        String courseName = null;
        Double gradeValue = null;
        LocalDate gradedAt = null;
        Long id = null;

        courseName = enrollmentCourseCourseName( enrollment );
        gradeValue = enrollment.getGradeValue();
        gradedAt = enrollment.getGradedAt();
        id = enrollment.getId();

        String studentName = formatStudentName(enrollment);
        String teacherName = formatTeacherName(enrollment);

        EnrollmentResponseDto enrollmentResponseDto = new EnrollmentResponseDto( id, studentName, courseName, teacherName, gradeValue, gradedAt );

        return enrollmentResponseDto;
    }

    @Override
    public Enrollment toEntity(EnrollmentRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        Enrollment enrollment = new Enrollment();

        enrollment.setGradeValue( dto.gradeValue() );
        enrollment.setGradedAt( dto.gradedAt() );

        return enrollment;
    }

    private String enrollmentCourseCourseName(Enrollment enrollment) {
        Course course = enrollment.getCourse();
        if ( course == null ) {
            return null;
        }
        return course.getCourseName();
    }
}
