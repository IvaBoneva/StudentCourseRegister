package com.tu.java_spring_project.demo.config.security;

import com.tu.java_spring_project.demo.model.Teacher;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class TeacherPrincipal implements UserDetails {

    private final Teacher teacher;

    public TeacherPrincipal(Teacher teacher) {
        this.teacher = teacher;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority("ROLE_" + teacher.getRole().name())
        );
    }

    @Override
    public String getPassword() {
        return teacher.getPassword();
    }

    @Override
    public String getUsername() {
        return teacher.getEmail();
    }

    @Override
    public boolean isEnabled() {
        return teacher.isEnabled();
    }

    public Long getTeacherId() {
        return teacher.getId();
    }

    public String getEmail() {
        return teacher.getEmail();
    }
}
