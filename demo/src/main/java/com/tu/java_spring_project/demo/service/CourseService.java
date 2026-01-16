package com.tu.java_spring_project.demo.service;

import com.tu.java_spring_project.demo.dto.course.CourseRequestDto;
import com.tu.java_spring_project.demo.dto.course.CourseResponseDto;
import com.tu.java_spring_project.demo.mapper.CourseMapper;
import com.tu.java_spring_project.demo.model.Course;
import com.tu.java_spring_project.demo.model.Room;
import com.tu.java_spring_project.demo.repository.CourseRepo;
import com.tu.java_spring_project.demo.repository.RoomRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseService {

    private final CourseRepo courseRepo;
    private final CourseMapper courseMapper;
    private final RoomRepo roomRepo;

    // Връща лист с всички курсове
    public List<CourseResponseDto> getAllCourses() {
        return courseMapper.toCourseResponseDtoList(courseRepo.findAll());
    }

    // Връща курс по id
    public CourseResponseDto getCourseByIdOrThrow(Long id) {
        Course course = courseRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Course not found with id " + id));
        return courseMapper.toCourseResponseDto(course);
    }

    // Създава и запазва курс
    @Transactional
    public CourseResponseDto saveCourse(CourseRequestDto dto) {
        Course course = courseMapper.toCourse(dto);

        // Задължително сетване на room
        Room room = roomRepo.findById(dto.roomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));
        course.setRoom(room);

        course.setEnrollments(new ArrayList<>());
        Course saved = courseRepo.save(course);
        return courseMapper.toCourseResponseDto(saved);
    }

    // Редактира курс по id
    @Transactional
    public CourseResponseDto updateCourse(Long id, CourseRequestDto dto) {
        Course existing = courseRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Course not found with id " + id));

        Room room = roomRepo.findById(dto.roomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));
        existing.setRoom(room);

        existing.setCourseName(dto.courseName());
        existing.setCredits(dto.credits());

        Course updated = courseRepo.save(existing);
        return courseMapper.toCourseResponseDto(updated);
    }

    // Трие курс по id
    @Transactional
    public void deleteCourse(Long id) {
        if (!courseRepo.existsById(id)) {
            throw new NoSuchElementException("Course not found with id " + id);
        }
        courseRepo.deleteById(id);
    }

    public List<CourseResponseDto> getCoursesByTeacherId(Long teacherId) {
        return courseRepo.findAllByTeacherId(teacherId)
                .stream()
                .map(courseMapper::toCourseResponseDto)
                .toList();
    }


}

