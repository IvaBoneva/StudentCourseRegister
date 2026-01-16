package com.tu.java_spring_project.demo.repository_tests;

import com.tu.java_spring_project.demo.enums.AcademicYear;
import com.tu.java_spring_project.demo.model.Role;
import com.tu.java_spring_project.demo.model.Student;
import com.tu.java_spring_project.demo.repository.StudentRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class StudentRepoTest1 {

    @Autowired
    private StudentRepo studentRepo;

    @Test
    void findStudentByFullName_shouldReturnStudent() {
        Student student = new Student();
        student.setFirstName("Marina");
        student.setLastName("Petrova");
        student.setFacultyNumber("450081511");
        student.setEmail("petrova@example.com");
        student.setAcademicYear(AcademicYear.FIRST);
        student.setRole(Role.STUDENT);


        studentRepo.save(student);

        Optional<Student> result =
                studentRepo.findStudentByFullName("Marina", "Petrova");

        assertTrue(result.isPresent());
        assertEquals("petrova@example.com", result.get().getEmail());
    }
}

