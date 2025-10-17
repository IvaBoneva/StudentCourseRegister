package com.tu.java_spring_project.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
@Table(name = "student")
public class Student {
    @Id
    private long id;
    private String name;
}
