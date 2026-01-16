package com.tu.java_spring_project.demo.startup.seeder;

import com.tu.java_spring_project.demo.enums.AcademicYear;
import com.tu.java_spring_project.demo.model.Role;
import com.tu.java_spring_project.demo.model.Student;
import com.tu.java_spring_project.demo.model.Teacher;
import com.tu.java_spring_project.demo.repository.StudentRepo;
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
    private final StudentRepo studentRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        if (teacherRepo.existsByEmail("admin@example.com")) {
            log.info("Admin already exists");
            return;
        }

        Teacher admin = Teacher.builder()
                .firstName("Maria")
                .lastName("Dimitrova")
                .email("admin@example.com")
                .password(passwordEncoder.encode("VeryStrongPass#100"))
                .role(Role.ADMIN)
                .enabled(true)
                .activationToken(null)
                .build();

        teacherRepo.save(admin);

        log.info("Admin teacher created: {}", admin.getEmail());

        if (teacherRepo.existsByEmail("ip@example.com")) {
            log.info("Teacher already exists");
            return;
        }

        Teacher teacher = Teacher.builder()
                .firstName("Ivan")
                .lastName("Petrov")
                .email("ip@example.com")
                .password(passwordEncoder.encode("VeryStrongPass#99"))
                .role(Role.TEACHER)
                .enabled(true)
                .activationToken(null)
                .build();

        teacherRepo.save(teacher);

        log.info("Teacher created: {}", teacher.getEmail());

        if (studentRepo.existsByEmail("lg@example.com")) {
            log.info("Student already exists");
            return;
        }

        Student student = Student.builder()
                .firstName("Lili")
                .lastName("Georgieva")
                .email("lg@example.com")
                .password(passwordEncoder.encode("Very444ongPass#55"))
                .facultyNumber("471223001")
                .academicYear(AcademicYear.FIRST)
                .role(Role.STUDENT)
                .enabled(true)
                .activationToken(null)
                .build();

        studentRepo.save(student);

        log.info("Student created: {}", student.getEmail());
    }

}


