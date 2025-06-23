package com.controller;

import com.dto.grade.GradeResponse;
import com.service.GradeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/calculate")
public class GradeController {
    private final GradeService gradeService;

    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @PostMapping("/calculate-student-score")
    public ResponseEntity<?> calculateStudentScore(@RequestParam String tutorId, @RequestParam UUID courseId, @RequestParam String studentId) {
        try {
            GradeResponse response = gradeService.calculateFinalGrade(tutorId, courseId, studentId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
