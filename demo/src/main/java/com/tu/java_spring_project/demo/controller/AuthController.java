package com.tu.java_spring_project.demo.controller;


import com.tu.java_spring_project.demo.config.security.JwtProvider;
import com.tu.java_spring_project.demo.config.security.StudentPrincipal;
import com.tu.java_spring_project.demo.config.security.TeacherPrincipal;
import com.tu.java_spring_project.demo.dto.auth.*;
import com.tu.java_spring_project.demo.dto.auth.student.StudentLoginRequestDTO;
import com.tu.java_spring_project.demo.dto.auth.student.StudentLoginResponseDTO;
import com.tu.java_spring_project.demo.dto.auth.student.StudentRegisterRequestDTO;
import com.tu.java_spring_project.demo.dto.auth.student.StudentRegisterResponseDTO;
import com.tu.java_spring_project.demo.dto.auth.teacher.TeacherLoginRequestDTO;
import com.tu.java_spring_project.demo.dto.auth.teacher.TeacherLoginResponseDTO;
import com.tu.java_spring_project.demo.dto.auth.teacher.TeacherRegisterRequestDTO;
import com.tu.java_spring_project.demo.dto.auth.teacher.TeacherRegisterResponseDTO;
import com.tu.java_spring_project.demo.mapper.StudentMapper;
import com.tu.java_spring_project.demo.mapper.TeacherMapper;
import com.tu.java_spring_project.demo.model.Student;
import com.tu.java_spring_project.demo.model.Teacher;
import com.tu.java_spring_project.demo.repository.StudentRepo;
import com.tu.java_spring_project.demo.repository.TeacherRepo;
import com.tu.java_spring_project.demo.service.StudentService;
import com.tu.java_spring_project.demo.service.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final TeacherService teacherService;
    private final StudentService studentService;
    private final TeacherMapper teacherMapper;
    private final StudentMapper studentMapper;
    private final TeacherRepo teacherRepo;
    private final StudentRepo studentRepo;
    private final PasswordEncoder passwordEncoder;

    @PreAuthorize("hasRole('ADMIN')") //only admins can register a teacher
    @PostMapping("/register/teacher")
    public ResponseEntity<TeacherRegisterResponseDTO> registerTeacher(
            @Valid @RequestBody TeacherRegisterRequestDTO registerRequestDTO) {

        Teacher teacher = teacherService.registerTeacher(registerRequestDTO);

        TeacherRegisterResponseDTO response =
                teacherMapper.toTeacherRegisterResponseDto(teacher);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/activate/teacher")
    public void activateTeacher(@RequestBody ActivateRequestDTO req) {
        Teacher teacher = teacherRepo
                .findByActivationToken(req.token())
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        teacher.setPassword(passwordEncoder.encode(req.password()));
        teacher.setEnabled(true);
        teacher.setActivationToken(null);

        teacherRepo.save(teacher);
    }

    @PostMapping("/login/teacher")
    public ResponseEntity<TeacherLoginResponseDTO> loginTeacher(
            @Valid @RequestBody TeacherLoginRequestDTO loginRequestDTO) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.email(),
                        loginRequestDTO.password()
                )
        );

        TeacherPrincipal principal = (TeacherPrincipal) authentication.getPrincipal();

        String token = jwtProvider.generateToken(
                principal.getEmail(), //instead of getEmail()
                principal.getAuthorities().iterator().next().getAuthority()
        );

        return ResponseEntity.ok(new TeacherLoginResponseDTO(token));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    @PostMapping("/register/student")
    public ResponseEntity<StudentRegisterResponseDTO> registerStudent(
            @Valid @RequestBody StudentRegisterRequestDTO registerRequestDTO) {

        Student student = studentService.registerStudent(registerRequestDTO);

        StudentRegisterResponseDTO registerResponseDTO =
                studentMapper.toStudentRegisterResponseDTO(student);

        return new ResponseEntity<>(registerResponseDTO, HttpStatus.CREATED);
    }

    @PostMapping("/activate/student")
    public void activateStudent(@RequestBody ActivateRequestDTO req) {
        Student student = studentRepo
                .findByActivationToken(req.token())
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        student.setPassword(passwordEncoder.encode(req.password()));
        student.setEnabled(true);
        student.setActivationToken(null);

        studentRepo.save(student);
    }

    @PostMapping("/login/student")
    public ResponseEntity<StudentLoginResponseDTO> loginStudent(
            @Valid @RequestBody StudentLoginRequestDTO loginRequestDTO) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.facultyNumber(),
                        loginRequestDTO.password()
                )
        );

        StudentPrincipal principal = (StudentPrincipal) authentication.getPrincipal();

        String token = jwtProvider.generateToken(
                principal.getFacultyNumber(), //instead of fetFacultyNumber()
                principal.getAuthorities().iterator().next().getAuthority()
        );

        return ResponseEntity.ok(new StudentLoginResponseDTO(token));
    }

    @PostMapping("/change-password/teacher")
    public ResponseEntity<Void> changeTeacherPassword(
            @RequestBody @Valid ChangePasswordRequestDTO request) {

        teacherService.changePasswordForTeacher(
                request.identifier(),
                request.oldPassword(),
                request.newPassword(),
                passwordEncoder
        );

        return ResponseEntity.ok().build();
    }

    @PostMapping("/change-password/student")
    public ResponseEntity<Void> changeStudentPassword(
            @RequestBody @Valid ChangePasswordRequestDTO request) {

        studentService.changePasswordForStudent(
                request.identifier(),
                request.oldPassword(),
                request.newPassword(),
                passwordEncoder
        );

        return ResponseEntity.ok().build();
    }

}

