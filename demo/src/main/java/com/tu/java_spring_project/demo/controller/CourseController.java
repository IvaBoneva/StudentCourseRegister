package com.tu.java_spring_project.demo.controller;

import com.tu.java_spring_project.demo.dto.CourseResponseDto;
import com.tu.java_spring_project.demo.dto.CourseRequestDto;
import com.tu.java_spring_project.demo.dto.CourseResponseDto;
import com.tu.java_spring_project.demo.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    // GET /api/courses
    @GetMapping
    public List<CourseResponseDto> getAllCourses() {
        return courseService.getAllCourses();
    }

    // GET /api/courses/{id}
    @GetMapping("/{id}")
    public CourseResponseDto getCourseById(@PathVariable Long id) {
        return courseService.getCourseByIdOrThrow(id);
    }

    // POST /api/courses/save
    @PostMapping("/save")
    public ResponseEntity<CourseResponseDto> saveCourse(@RequestBody CourseRequestDto dto) {
        CourseResponseDto saved = courseService.saveCourse(dto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // PUT /api/courses/{id}
    @PutMapping("/{id}")
    public ResponseEntity<CourseResponseDto> updateCourse(
            @PathVariable Long id,
            @RequestBody CourseRequestDto dto) {
        CourseResponseDto updated = courseService.updateCourse(id, dto);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    // DELETE /api/courses/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}