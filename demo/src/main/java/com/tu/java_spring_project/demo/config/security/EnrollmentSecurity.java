package com.tu.java_spring_project.demo.config.security;

import com.tu.java_spring_project.demo.repository.EnrollmentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("enrollmentSecurity")
@RequiredArgsConstructor
public class EnrollmentSecurity {

        private final EnrollmentRepo enrollmentRepo;

    /**
     * Returns true if the teacher leads the course in the given enrollment
     */
    public boolean isTeacherOfEnrollment(Long teacherId, Long enrollmentId) {
        return enrollmentRepo.existsByIdAndTeacherId(enrollmentId, teacherId);
    }

    /**
     * Returns true if the teacher teaches any course the student is enrolled in
     */
    public boolean canAccessStudent(Long teacherId, Long studentId) {
        return enrollmentRepo.existsByTeacherIdAndStudentId(teacherId, studentId);
    }

    /**
     * Returns true if the student is accessing their own data
     */
    public boolean isSelfStudent(Long studentId, Long principalStudentId) {
        return studentId != null && studentId.equals(principalStudentId);
    }

    /**
     * Returns true if the teacher leads the given course
     */
    public boolean isTeacherOfCourse(Long teacherId, Long courseId) {
        return enrollmentRepo.existsByCourseIdAndTeacherId(courseId, teacherId);
    }

    /**
     * Returns true if the enrollment belongs to the student
     */
    public boolean isStudentOfEnrollment(Long studentId, Long enrollmentId) {
        return enrollmentRepo.existsByIdAndStudentId(enrollmentId, studentId);
    }


}

