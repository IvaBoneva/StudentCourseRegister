package com.tu.java_spring_project.demo.config.security;

import com.tu.java_spring_project.demo.repository.CourseRepo;
import com.tu.java_spring_project.demo.repository.EnrollmentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component("enrollmentSecurity")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@RequiredArgsConstructor
public class AccessSecurity {

    private final EnrollmentRepo enrollmentRepo;
    private final CourseRepo courseRepo;

    public boolean canAccessStudent(Object principal, Long studentId) {

        if (principal instanceof TeacherPrincipal tp) {
            return enrollmentRepo.existsByTeacherIdAndStudentId(
                    tp.getTeacherId(), studentId
            );
        }

        if (principal instanceof StudentPrincipal sp) {
            return studentId != null && studentId.equals(sp.getStudentId());
        }

        return false;
    }

    public boolean canAccessEnrollment(Object principal, Long enrollmentId) {

        if (principal instanceof TeacherPrincipal tp) {
            return enrollmentRepo.existsByIdAndTeacherId(
                    enrollmentId, tp.getTeacherId()
            );
        }

        if (principal instanceof StudentPrincipal sp) {
            return enrollmentRepo.existsByIdAndStudentId(
                    enrollmentId, sp.getStudentId()
            );
        }

        return false;
    }

    public boolean canAccessCourse(Object principal, Long courseId) {

        if (principal instanceof TeacherPrincipal tp) {
            return courseRepo.existsByIdAndTeachersId(
                    courseId, tp.getTeacherId()
            );
        }

        return false;
    }

    public boolean canAccessTeacher(Object principal, Long teacherId) {
        if (principal instanceof TeacherPrincipal tp) {
            // Teacher can access their own data
            return tp.getTeacherId().equals(teacherId);
        }
        // Admins are handled in PreAuthorize itself
        return false;
    }

}

