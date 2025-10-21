package com.tu.java_spring_project.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity // to save class in DB
@Table(name = "teachers") // table in DB
@Data // auto-generated getters, setters, constructors and some methods
@NoArgsConstructor // empty constructor
public class Teacher {
    @Id // primary key
    @Column(name = "teacher_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-increment
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) // in JSON response, but not in POST / PUT
    private Long id;

    @Column(name = "first_name", nullable = false) // column in DB, can't be null
    private String firstName;

    @Column(name = "last_name", nullable = false) // column in DB, can't be null
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    // One Course -> Many Teachers; One Teacher -> Many Courses
    @ManyToMany(mappedBy = "teachers") // teachers в Course е собственик на връзката,
    private List<Course> courses;      // т.е. course_teachers join таблицата се дефинира там

    // One Teacher -> Many Enrollments
    @OneToMany(mappedBy = "teacher")
    private List<Enrollment> enrollments;
}
