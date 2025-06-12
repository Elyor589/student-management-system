package com.controller;

import com.dto.EnrollmentDto;
import com.service.EnrollmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/enrollments")
public class EnrollmentController {
    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping("/getAllEnrollments")
    public ResponseEntity<List<EnrollmentDto>> getAllEnrollments() {
        return new ResponseEntity<>(enrollmentService.getAllEnrollments(), HttpStatus.OK);
    }

    @GetMapping("/getEnrollmentsByStudentId")
    public ResponseEntity<List<EnrollmentDto>> getEnrollmentsByStudentId(@RequestParam("studentId") String studentId) {
        List<EnrollmentDto> enrollmentDto = enrollmentService.getEnrollmentByStudentId(studentId);
        return ResponseEntity.ok(enrollmentDto);
    }

    @GetMapping("/getEnrollmentsByCourseId")
    public ResponseEntity<List<EnrollmentDto>> getEnrollmentsByCourseId(@RequestParam("courseId") UUID courseId) {
        List<EnrollmentDto> enrollmentDto = enrollmentService.getEnrollmentByCourseId(courseId);
        return ResponseEntity.ok(enrollmentDto);
    }

    @PostMapping("/enroll/student{studentId}/course{courseId}")
    public ResponseEntity<EnrollmentDto> enrollStudent(@PathVariable String studentId, @PathVariable UUID courseId) {
        EnrollmentDto enrollment = enrollmentService.enroll(studentId, courseId);
        return ResponseEntity.ok(enrollment);
    }
}
