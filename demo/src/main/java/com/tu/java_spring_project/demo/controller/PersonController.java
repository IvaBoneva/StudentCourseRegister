package com.tu.java_spring_project.demo.controller;

import com.tu.java_spring_project.demo.model.Student;
import com.tu.java_spring_project.demo.repository.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PersonController {

    @Autowired
    StudentRepo studentRepo;

    @PostMapping("/addStudent")
    public void addStudent(@RequestBody Student student) {
        studentRepo.save(student);
    }
}
