package com.tu.java_spring_project.demo.repository_tests;

import com.tu.java_spring_project.demo.enums.AcademicYear;
import com.tu.java_spring_project.demo.model.Student;
import com.tu.java_spring_project.demo.repository.StudentRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class StudentRepoTest2 {

    @Autowired
    private StudentRepo studentRepo;

    @Test
    void findStudentByFacultyNumber_shouldReturnStudent() {
        Student student = new Student();
        student.setFirstName("Stoyan");
        student.setLastName("Kanev");
        student.setFacultyNumber("450067890");
        student.setEmail("kanevS@example.com");
        student.setAcademicYear(AcademicYear.THIRD);

        studentRepo.save(student);

        Optional<Student> result =
                studentRepo.findStudentByFacultyNumber("450067890");

        assertTrue(result.isPresent());
        assertEquals("kanevS@example.com", result.get().getEmail());
    }
}
