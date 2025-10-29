package com.tu.java_spring_project.demo.mapper;

import com.tu.java_spring_project.demo.dto.StudentRequestDto;
import com.tu.java_spring_project.demo.dto.StudentResponseDto;
import com.tu.java_spring_project.demo.enums.AcademicYear;
import com.tu.java_spring_project.demo.model.Student;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-29T14:31:52+0200",
    comments = "version: 1.6.0, compiler: javac, environment: Java 17.0.16 (Eclipse Adoptium)"
)
@Component
public class StudentMapperImpl implements StudentMapper {

    @Override
    public StudentResponseDto toStudentResponseDto(Student student) {
        if ( student == null ) {
            return null;
        }

        List<Long> enrollmentIds = null;
        List<String> courseNames = null;
        Long id = null;
        String firstName = null;
        String lastName = null;
        String facultyNumber = null;
        String email = null;
        AcademicYear academicYear = null;

        enrollmentIds = mapEnrollmentsToIds( student.getEnrollments() );
        courseNames = mapEnrollmentsToCourseNames( student.getEnrollments() );
        id = student.getId();
        firstName = student.getFirstName();
        lastName = student.getLastName();
        facultyNumber = student.getFacultyNumber();
        email = student.getEmail();
        academicYear = student.getAcademicYear();

        StudentResponseDto studentResponseDto = new StudentResponseDto( id, firstName, lastName, facultyNumber, email, academicYear, enrollmentIds, courseNames );

        return studentResponseDto;
    }

    @Override
    public Student toStudent(StudentRequestDto studentRequestDto) {
        if ( studentRequestDto == null ) {
            return null;
        }

        Student student = new Student();

        student.setFirstName( studentRequestDto.firstName() );
        student.setLastName( studentRequestDto.lastName() );
        student.setFacultyNumber( studentRequestDto.facultyNumber() );
        student.setEmail( studentRequestDto.email() );
        student.setAcademicYear( studentRequestDto.academicYear() );

        return student;
    }
}
