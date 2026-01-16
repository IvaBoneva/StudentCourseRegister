package com.tu.java_spring_project.demo.startup.seeder;

import com.tu.java_spring_project.demo.model.Role;
import com.tu.java_spring_project.demo.model.Teacher;
import com.tu.java_spring_project.demo.repository.TeacherRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Slf4j
@Profile("dev")
@Order(1)
@Component
@RequiredArgsConstructor
public class AdminTeacherSeeder implements CommandLineRunner {

    private final TeacherRepo teacherRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        if (teacherRepo.existsByEmail("admin@example.com")) {
            log.info("Admin teacher already exists");
            return;
        }

        Teacher teacher = Teacher.builder()
                .firstName("Maria")
                .lastName("Dimitrova")
                .email("admin@example.com")
                .password(passwordEncoder.encode("VeryStrongPass#100"))
                .role(Role.ADMIN)
                .enabled(true)
                .activationToken(null)
                .build();

        teacherRepo.save(teacher);

        log.info("Admin teacher created: {}", teacher.getEmail());
    }
}


