package com.tu.java_spring_project.demo.repository;

import com.tu.java_spring_project.demo.model.Student;
import com.tu.java_spring_project.demo.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepo extends JpaRepository<Student, Long> {

    @Query("""
        SELECT s
        FROM Student s 
        WHERE s.firstName = :firstName
          AND s.lastName = :lastName
    """)
    Optional<Student> findStudentByFullName(
            @Param("firstName") String firstName,
            @Param("lastName") String lastName
    );

    @Query("""
        SELECT s
        FROM Student s
        WHERE s.facultyNumber = :facultyNumber
    """)
    Optional<Student> findStudentByFacultyNumber(
            @Param("facultyNumber") String facultyNumber
    );

    boolean existsByFacultyNumber(String facultyNumber);

    boolean existsByEmail(String email);

    Optional<Student> findByActivationToken(String activationToken);
}
