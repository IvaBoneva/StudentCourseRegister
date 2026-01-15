package com.tu.java_spring_project.demo.validation_tests;


import com.tu.java_spring_project.demo.enums.AcademicYear;
import com.tu.java_spring_project.demo.model.Room;
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

class RoomValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // Тест за невалидни capacity стойности
    @ParameterizedTest
    @ValueSource(ints = { 6, 200, 0, 19, 101 })
    void capacity_shouldFailValidation_forInvalidValues(int capacity) {
        Room room = new Room();
        room.setCapacity(capacity);

        Set<ConstraintViolation<Room>> violations = validator.validate(room);

        assertFalse(violations.isEmpty(), "Expected violations for capacity: " + capacity);
    }

    // Тест за валидна capacity стойност
    @ParameterizedTest
    @ValueSource(ints = { 20, 100, 50 })
    void capacity_shouldPassValidation_forValidValues(int capacity) {
        Room room = new Room();
        room.setCapacity(capacity);

        Set<ConstraintViolation<Room>> violations = validator.validate(room);

        assertTrue(violations.isEmpty(), "Expected no violations for capacity: " + capacity);
    }
}
