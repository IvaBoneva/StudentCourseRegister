package com.tu.java_spring_project.demo.config.security;

import com.tu.java_spring_project.demo.model.Student;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class StudentPrincipal implements UserDetails {

    private final Student student;

    public StudentPrincipal (Student student) {
        this.student = student;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {

        return List.of(
                new SimpleGrantedAuthority("ROLE_" + student.getRole().name())
        );
    }

    @Override
    public String getPassword() { return student.getPassword(); }

    @Override
    public String getUsername() { return student.getFacultyNumber(); }

    public Long getStudentId() { return student.getId(); }

    public String getFacultyNumber() { return student.getFacultyNumber(); }

    @Override
    public boolean isEnabled() { return student.isEnabled(); }

}
