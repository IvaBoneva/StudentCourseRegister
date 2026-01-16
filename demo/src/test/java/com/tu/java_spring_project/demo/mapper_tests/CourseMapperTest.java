package com.tu.java_spring_project.demo.mapper_tests;

import com.tu.java_spring_project.demo.dto.course.CourseResponseDto;
import com.tu.java_spring_project.demo.enums.AcademicYear;
import com.tu.java_spring_project.demo.mapper.CourseMapper;
import com.tu.java_spring_project.demo.model.*;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CourseMapperTest {

    private final CourseMapper courseMapper = Mappers.getMapper(CourseMapper.class);

    private Room createRoom(Long id, int capacity){
        Room room = new Room();
        room.setId(id);
        room.setCapacity(capacity);
        return room;
    }

    private Student createStudent(Long id, String firstName, String lastName, String facultyNumber, String email){
        return Student.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .facultyNumber(facultyNumber)
                .email(email)
                .academicYear(AcademicYear.FIRST)
                .role(com.tu.java_spring_project.demo.model.Role.STUDENT)
                .enabled(true)
                .build();
    }

    private Teacher createTeacher(Long id, String firstName, String lastName, String email){
        return Teacher.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .role(com.tu.java_spring_project.demo.model.Role.TEACHER)
                .enabled(true)
                .build();
    }

    private Course createCourse(Long id, String courseName, int credits, Room room){
        Course course = new Course();
        course.setId(id);
        course.setCourseName(courseName);
        course.setCredits(credits);
        course.setRoom(room);
        return course;
    }

    private Enrollment createEnrollment(Long id, Student student, Course course, Teacher teacher, double gradeValue, LocalDate gradedAt){
        Enrollment enrollment = new Enrollment();
        enrollment.setId(id);
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setTeacher(teacher);
        enrollment.setGradeValue(gradeValue);
        enrollment.setGradedAt(gradedAt);
        return enrollment;
    }

    @Test
    void testCourseResponseDto_WithCompleteData(){
        // GIVEN
        Room room = createRoom(1L, 50);

        Student student1 = createStudent(1L, "Bilqna", "Hristova", "901234577", "bilqna@example.com");
        Student student2 = createStudent(2L, "Janet", "Filipova", "901234578", "janet@example.com");

        Teacher teacher1 = createTeacher(1L, "Prof", "Stefanov", "stefanov@example.com");
        Teacher teacher2 = createTeacher(2L, "Dr", "Berry", "berry@example.com");

        Course course = createCourse(1L, "Math", 7, room);

        Enrollment enrollment1 = createEnrollment(1L, student1, course, teacher1, 5.0, LocalDate.of(2026, 1, 10));
        Enrollment enrollment2 = createEnrollment(2L, student2, course, teacher2, 4.5, LocalDate.of(2026, 1, 12));

        course.setEnrollments(List.of(enrollment1, enrollment2));

        // WHEN
        CourseResponseDto result = courseMapper.toCourseResponseDto(course);

        // THEN
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Math", result.courseName());
        assertEquals(7, result.credits());
        assertEquals(1L, result.roomId());

        assertNotNull(result.studentNames());
        assertEquals(2, result.studentNames().size());
        assertTrue(result.studentNames().contains("Bilqna Hristova"));
        assertTrue(result.studentNames().contains("Janet Filipova"));

        assertNotNull(result.teacherNames());
        assertEquals(2, result.teacherNames().size());
        assertTrue(result.teacherNames().contains("Prof Stefanov"));
        assertTrue(result.teacherNames().contains("Dr Berry"));
    }

    @Test
    void testCourseResponseDto_WithNullRoom(){
        // GIVEN
        Course course = createCourse(1L, "Physics", 7, null);

        // WHEN
        CourseResponseDto result = courseMapper.toCourseResponseDto(course);

        // THEN
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Physics", result.courseName());
        assertEquals(7, result.credits());
        assertNull(result.roomId());
        assertNotNull(result.studentNames());
        assertNotNull(result.teacherNames());
    }

    @Test
    void testCourseResponseDto_WithEmptyEnrollments(){
        // GIVEN
        Room room = createRoom(2L, 50);
        Course course = createCourse(1L, "Algebra", 7, room);
        course.setEnrollments(List.of());

        // WHEN
        CourseResponseDto result = courseMapper.toCourseResponseDto(course);

        // THEN
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Algebra", result.courseName());
        assertEquals(7, result.credits());

        assertNotNull(result.studentNames());
        assertTrue(result.studentNames().isEmpty());

        assertNotNull(result.teacherNames());
        assertTrue(result.teacherNames().isEmpty());
    }

    @Test
    void testCourseResponseDto_WithDuplicateStudentAndTeacher(){
        // GIVEN
        Room room = createRoom(3L, 40);

        Student student = createStudent(1L, "Stoqn", "Stoqnov", "111222333", "stoqn@example.com");
        Teacher teacher = createTeacher(1L, "Prof", "Toshev", "toshev@example.com");

        Course course = createCourse(3L, "IT", 5, room);

        Enrollment enrollment1 = createEnrollment(1L, student, course, teacher, 5.0, LocalDate.of(2026, 1, 10));
        Enrollment enrollment2 = createEnrollment(2L, student, course, teacher, 4.5, LocalDate.of(2026, 1, 12));

        course.setEnrollments(List.of(enrollment1, enrollment2));

        // WHEN
        CourseResponseDto result = courseMapper.toCourseResponseDto(course);

        // THEN
        assertNotNull(result);
        assertEquals(3L, result.id());
        assertEquals("IT", result.courseName());
        assertEquals(5, result.credits());
        assertEquals(3L, result.roomId());

        assertNotNull(result.studentNames());
        assertEquals(1, result.studentNames().size());
        assertTrue(result.studentNames().contains("Stoqn Stoqnov"));

        assertNotNull(result.teacherNames());
        assertEquals(1, result.teacherNames().size());
        assertTrue(result.teacherNames().contains("Prof Toshev"));
    }



}
