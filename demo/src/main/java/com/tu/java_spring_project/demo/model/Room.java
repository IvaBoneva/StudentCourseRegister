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
@Table(name = "rooms") // table in DB
@Data // auto-generated getters, setters, constructors and some methods
@NoArgsConstructor // empty constructor
public class Room {
    @Id // primary key
    @Column(name = "room_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-increment
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) // in JSON response, but not in POST / PUT
    private Long id;

    @Min(20)
    @Max(100)
    @Positive
    @Column(nullable = false) // column in DB, can't be null
    private int capacity;

    // One Room -> Many Courses
    @OneToMany(mappedBy = "room")
    private List<Course> courses;
}