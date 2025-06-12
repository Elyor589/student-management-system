package com.controller;

import com.dto.assignment.RequestAssignment;
import com.dto.assignment.ResponseAssignment;
import com.service.AssignmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/assignments")
public class AssignmentController {
    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @PostMapping("/create-assignment")
    public ResponseEntity<?> createAssignment(@RequestParam String tutorId, @RequestParam UUID courseId, @RequestBody RequestAssignment requestAssignment){
        try {
            ResponseAssignment response = assignmentService.createAssignment(tutorId, courseId, requestAssignment);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
