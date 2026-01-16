package com.tu.java_spring_project.demo.service;

import com.tu.java_spring_project.demo.config.security.StudentPrincipal;
import com.tu.java_spring_project.demo.config.security.TeacherPrincipal;
import com.tu.java_spring_project.demo.repository.StudentRepo;
import com.tu.java_spring_project.demo.repository.TeacherRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

private final TeacherRepo teacherRepo;
private final StudentRepo studentRepo;

    @Override
    public UserDetails loadUserByUsername(String username) {

        return teacherRepo.findTeacherByEmail(username)
                .<UserDetails>map(TeacherPrincipal::new)
                .orElseGet(() ->
                        studentRepo.findStudentByFacultyNumber(username)
                                .map(StudentPrincipal::new)
                                .orElseThrow(() ->
                                        new UsernameNotFoundException("User not found")
                                )
                );
    }
}






