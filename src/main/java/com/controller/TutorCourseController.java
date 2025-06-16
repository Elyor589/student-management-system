package com.controller;

import com.dto.TutorCourseRequestDto;
import com.entity.TutorCourse;
import com.service.TutorCourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/tutor-courses")
public class TutorCourseController {
    private final TutorCourseService tutorCourseService;

    public TutorCourseController(TutorCourseService tutorCourseService) {
        this.tutorCourseService = tutorCourseService;
    }

    @PostMapping("/assignTutorToCourse")
    public ResponseEntity<?> assignTutorToCourse(@RequestParam UUID courseId, @RequestParam String tutorId, @RequestBody TutorCourseRequestDto tutorCourseRequestDto) {
        try {
            TutorCourse tutorCourse = tutorCourseService.assignTutorToCourse(courseId, tutorId, tutorCourseRequestDto);
            return new ResponseEntity<>(tutorCourse, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
