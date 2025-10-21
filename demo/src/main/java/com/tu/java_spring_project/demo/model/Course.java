package com.tu.java_spring_project.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity // to save class in DB
@Table(name = "courses") // table in DB
@Data // auto-generated getters, setters, constructors and some methods
@NoArgsConstructor // empty constructor

public class Course {
    @Id // primary key
    @Column(name = "course_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-increment
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) // in JSON response, but not in POST / PUT
    private Long id;

    @Column(name = "course_name", nullable = false) // column in DB, can't be null
    private String courseName;

    @Min(1)
    @Max(10)
    @Positive
    @Column(nullable = false) // column in DB, can't be null
    private int credits;

    // One Course -> Many Teachers; One Teacher -> Many Courses
    @ManyToMany
    @JoinTable(
            name = "course_teachers",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "teacher_id")
    )
    private List<Teacher> teachers;

    // Many Courses -> One Room
    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    // One Course -> Many Enrollments
    @OneToMany(mappedBy = "course")
    private List<Enrollment> enrollments;
}
