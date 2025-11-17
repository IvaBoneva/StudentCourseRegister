package com.tu.java_spring_project.demo.repository;

import com.tu.java_spring_project.demo.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GradeRepo extends JpaRepository<Grade, Long> {
}