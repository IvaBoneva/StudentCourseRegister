package com.tu.java_spring_project.demo.repository;

import com.tu.java_spring_project.demo.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepo extends JpaRepository<Teacher, Long> {

    @Query("""
        SELECT t
        FROM Teacher t
        WHERE t.firstName = :firstName
          AND t.lastName = :lastName
    """)
    Optional<Teacher> findTeacherByFullName(
            @Param("firstName") String firstName,
            @Param("lastName") String lastName
    );
}
