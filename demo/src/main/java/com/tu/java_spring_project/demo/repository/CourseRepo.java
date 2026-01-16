package com.tu.java_spring_project.demo.repository;

import com.tu.java_spring_project.demo.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepo extends JpaRepository<Course, Long> {

    @Query("""
        SELECT c
        FROM Course c
        WHERE c.courseName = :courseName
    """)
    Optional<Course> findCourseByName(@Param("courseName") String courseName);

    // All courses led by a teacher
    List<Course> findAllByTeachersId(Long teacherId);

    // Used for security checks
    boolean existsByIdAndTeachersId(Long courseId, Long teacherId);

}
