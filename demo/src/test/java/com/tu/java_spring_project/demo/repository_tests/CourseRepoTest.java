package com.tu.java_spring_project.demo.repository_tests;

import com.tu.java_spring_project.demo.enums.AcademicYear;
import com.tu.java_spring_project.demo.model.Course;
import com.tu.java_spring_project.demo.model.Room;
import com.tu.java_spring_project.demo.repository.CourseRepo;
import com.tu.java_spring_project.demo.repository.RoomRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class CourseRepoTest {

    @Autowired
    private CourseRepo courseRepo;

    @Autowired
    private RoomRepo roomRepo;

    @Test
    void findCourseByName_shouldReturnCourse() {
        // GIVEN room
        Room room = new Room();
        room.setCapacity(50);
        room = roomRepo.save(room);

        // GIVEN course
        Course course = new Course();
        course.setCourseName("Data Analysis");
        course.setCredits(7);
        course.setRoom(room);

        courseRepo.save(course);

        // WHEN
        Optional<Course> result =
                courseRepo.findCourseByName("Data Analysis");

        // THEN
        assertTrue(result.isPresent());
        assertEquals("Data Analysis", result.get().getCourseName());
        assertEquals(7, result.get().getCredits());
    }
}
