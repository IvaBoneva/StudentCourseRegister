package com.tu.java_spring_project.demo.repository;

import com.tu.java_spring_project.demo.model.Course;
import com.tu.java_spring_project.demo.model.Enrollment;
import com.tu.java_spring_project.demo.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentRepo extends JpaRepository<Enrollment, Long> {
    boolean existsByStudentAndCourse(Student student, Course course);

    boolean existsByIdAndTeacherId(Long enrollmentId, Long teacherId);

    boolean existsByTeacherIdAndStudentId(Long teacherId, Long studentId);

    boolean existsByCourseIdAndTeacherId(Long courseId, Long teacherId);

    boolean existsByIdAndStudentId(Long enrollmentId, Long studentId);

}
