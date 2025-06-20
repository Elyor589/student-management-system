package com.controller;

import com.dto.CourseDto;
import com.entity.Course;
import com.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/course")
public class CourseController {

    private final CourseService courseService;


    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/getAllCourses")
    public ResponseEntity<List<CourseDto>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @PostMapping("/create-course")
    public ResponseEntity<Course> createCourse(@RequestParam String title) {
        return ResponseEntity.ok(courseService.createCourse(title));
    }

    @DeleteMapping("/delete-course/{courseId}")
    public ResponseEntity<Void> deleteCourse(@PathVariable UUID courseId) {
        courseService.deleteCourse(courseId);
        return ResponseEntity.ok().build();
    }
}
