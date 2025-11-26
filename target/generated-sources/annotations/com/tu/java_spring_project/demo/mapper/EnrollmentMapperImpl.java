package com.tu.java_spring_project.demo.mapper;

import com.tu.java_spring_project.demo.dto.EnrollmentRequestDto;
import com.tu.java_spring_project.demo.dto.EnrollmentResponseDto;
import com.tu.java_spring_project.demo.model.Course;
import com.tu.java_spring_project.demo.model.Enrollment;
import com.tu.java_spring_project.demo.model.Grade;
import java.time.LocalDate;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-26T20:22:52+0200",
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
        Long id = null;
        LocalDate enrolledAt = null;

        courseName = enrollmentCourseCourseName( enrollment );
        gradeValue = enrollmentGradeGradeValue( enrollment );
        id = enrollment.getId();
        enrolledAt = enrollment.getEnrolledAt();

        String studentName = formatStudentName(enrollment);
        String teacherName = formatTeacherName(enrollment);

        EnrollmentResponseDto enrollmentResponseDto = new EnrollmentResponseDto( id, studentName, courseName, teacherName, gradeValue, enrolledAt );

        return enrollmentResponseDto;
    }

    @Override
    public Enrollment toEntity(EnrollmentRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        Enrollment enrollment = new Enrollment();

        enrollment.setEnrolledAt( dto.enrolledAt() );

        return enrollment;
    }

    private String enrollmentCourseCourseName(Enrollment enrollment) {
        Course course = enrollment.getCourse();
        if ( course == null ) {
            return null;
        }
        return course.getCourseName();
    }

    private Double enrollmentGradeGradeValue(Enrollment enrollment) {
        Grade grade = enrollment.getGrade();
        if ( grade == null ) {
            return null;
        }
        return grade.getGradeValue();
    }
}
