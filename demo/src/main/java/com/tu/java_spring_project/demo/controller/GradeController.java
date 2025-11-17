package com.tu.java_spring_project.demo.controller;

import com.tu.java_spring_project.demo.dto.GradeRequestDto;
import com.tu.java_spring_project.demo.dto.GradeResponseDto;
import com.tu.java_spring_project.demo.service.GradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/grades")
public class GradeController {

    private final GradeService gradeService;

    // GET /api/grades
    @GetMapping
    public List<GradeResponseDto> getAllGrades() {
        return gradeService.getAllGrades();
    }

    // GET /api/grades/{id}
    @GetMapping("/{id}")
    public GradeResponseDto getGradeById(@PathVariable Long id) {
        return gradeService.getGradeByIdOrThrow(id);
    }

    // POST /api/grades/save
    @PostMapping("/save")
    public ResponseEntity<GradeResponseDto> saveGrade(@RequestBody GradeRequestDto dto) {
        GradeResponseDto saved = gradeService.saveGrade(dto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // PUT /api/grades/{id}
    @PutMapping("/{id}")
    public ResponseEntity<GradeResponseDto> updateGrade(
            @PathVariable Long id,
            @RequestBody GradeRequestDto dto) {
        GradeResponseDto updated = gradeService.updateGrade(id, dto);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    // DELETE /api/grades/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGrade(@PathVariable Long id) {
        gradeService.deleteGrade(id);
        return ResponseEntity.noContent().build();
    }
}