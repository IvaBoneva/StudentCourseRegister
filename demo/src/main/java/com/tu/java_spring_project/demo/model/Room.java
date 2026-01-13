package com.tu.java_spring_project.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "rooms")
@Data
@NoArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotNull(message = "Capacity is required")
    @Min(value = 20, message = "Room capacity must be at least 20")
    @Max(value = 100, message = "Room capacity cannot exceed 100")
    @Column(nullable = false)
    private int capacity;

    // One Room -> Many Courses
    @OneToMany(mappedBy = "room")
    private List<Course> courses;
}
