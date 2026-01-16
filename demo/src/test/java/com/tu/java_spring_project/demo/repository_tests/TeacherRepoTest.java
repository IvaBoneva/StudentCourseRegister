package com.tu.java_spring_project.demo.repository_tests;

import com.tu.java_spring_project.demo.model.Teacher;
import com.tu.java_spring_project.demo.repository.TeacherRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class TeacherRepoTest {

    @Autowired
    private TeacherRepo teacherRepo;

    @Test
    void findTeacherByFullName_shouldReturnTeacher() {
        Teacher teacher = new Teacher();
        teacher.setFirstName("Ivan");
        teacher.setLastName("Ivanov");
        teacher.setEmail("ivan@example.com");

        teacherRepo.save(teacher);

        Optional<Teacher> result =
                teacherRepo.findTeacherByFullName("Ivan", "Ivanov");

        assertTrue(result.isPresent());
        assertEquals("ivan@example.com", result.get().getEmail());
    }
}
