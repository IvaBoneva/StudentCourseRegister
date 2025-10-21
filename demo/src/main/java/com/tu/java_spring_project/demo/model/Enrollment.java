package com.tu.java_spring_project.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity // to save class in DB
@Table(name = "enrollments") // table in DB
@Data // auto-generated getters, setters, constructors and some methods
@NoArgsConstructor // empty constructor
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enrollment_id")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) // in JSON response, but not in POST / PUT
    private Long id; // Enrollment Id

    // Many Enrollments -> One Student
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    // Many Enrollments -> One Course
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    // Many Enrollments -> One Teacher
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    // One Enrollment -> One Grade
    @OneToOne
    @JoinColumn(name = "grade_id")
    private Grade grade;

    // Дата на записване
    @Column(name = "enrolled_at", nullable = false)
    private LocalDate enrolledAt;
}
