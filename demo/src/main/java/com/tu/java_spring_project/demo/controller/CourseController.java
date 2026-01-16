package com.tu.java_spring_project.demo.controller;

import com.tu.java_spring_project.demo.dto.course.CourseResponseDto;
import com.tu.java_spring_project.demo.dto.course.CourseRequestDto;
import com.tu.java_spring_project.demo.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    // GET /api/courses
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<CourseResponseDto> getAllCourses() {
        return courseService.getAllCourses();
    }

    // GET /api/courses/{id}
    @PreAuthorize("""
    hasRole('ADMIN')
    or @enrollmentSecurity.isTeacherOfCourse(principal.teacherId, #id)
""")
    @GetMapping("/{id}")
    public CourseResponseDto getCourseById(@PathVariable Long id) {
        return courseService.getCourseByIdOrThrow(id);
    }

    // GET all courses for a teacher
    @PreAuthorize("hasRole('ADMIN') or principal.teacherId == #teacherId")
    @GetMapping("/teacher/{teacherId}")
    public List<CourseResponseDto> getCoursesByTeacher(@PathVariable Long teacherId) {
        return courseService.getCoursesByTeacherId(teacherId);
    }

    // POST /api/courses/save
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/save")
    public ResponseEntity<CourseResponseDto> saveCourse(@RequestBody CourseRequestDto dto) {
        CourseResponseDto saved = courseService.saveCourse(dto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // PUT /api/courses/{id}
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CourseResponseDto> updateCourse(
            @PathVariable Long id,
            @RequestBody CourseRequestDto dto) {
        CourseResponseDto updated = courseService.updateCourse(id, dto);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    // DELETE /api/courses/{id}
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}