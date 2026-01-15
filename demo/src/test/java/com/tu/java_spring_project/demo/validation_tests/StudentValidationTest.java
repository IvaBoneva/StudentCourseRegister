package com.tu.java_spring_project.demo.validation_tests;

import com.tu.java_spring_project.demo.enums.AcademicYear;
import com.tu.java_spring_project.demo.model.Student;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StudentValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // Тест за невалидни facultyNumber стойности
    @ParameterizedTest
    @ValueSource(strings = { "ABC123", "12345", "1234567890", "12A345678", "" })
    void facultyNumber_shouldFailValidation_forInvalidValues(String facultyNumber) {
        Student student = new Student();
        student.setFirstName("Kaloyan");
        student.setLastName("Gerasimov");
        student.setEmail("kala@example.com");
        student.setAcademicYear(AcademicYear.FIRST);
        student.setFacultyNumber(facultyNumber);

        Set<ConstraintViolation<Student>> violations = validator.validate(student);

        assertFalse(violations.isEmpty(), "Expected violations for facultyNumber: " + facultyNumber);
    }

    // Тест за валидна facultyNumber стойност
    @ParameterizedTest
    @ValueSource(strings = { "123456789", "987654321" })
    void facultyNumber_shouldPassValidation_forValidValues(String facultyNumber) {
        Student student = new Student();
        student.setFirstName("Doris");
        student.setLastName("Pavlova");
        student.setEmail("ddoris@example.com");
        student.setAcademicYear(AcademicYear.SECOND);
        student.setFacultyNumber(facultyNumber);

        Set<ConstraintViolation<Student>> violations = validator.validate(student);

        assertTrue(violations.isEmpty(), "Expected no violations for facultyNumber: " + facultyNumber);
    }
}
