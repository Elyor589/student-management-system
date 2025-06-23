package com.controller;

import com.dto.assignment.AssignmentSubmissionStats;
import com.dto.assignment.RequestAssignment;
import com.dto.assignment.ResponseAssignment;
import com.service.AssignmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping("/getAllAssignmentsByCourseId")
    public ResponseEntity<?> getAllAssignmentsByCourseId(@RequestParam UUID courseId){
        try {
            List<ResponseAssignment> allAssignmentsByCourseId = assignmentService.getAllAssignmentsByCourseId(courseId);
            return ResponseEntity.ok(allAssignmentsByCourseId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/getDeadlinePassedAssignmentsByCourseId")
    public ResponseEntity<?> getDeadlinePassedAssignmentsByCourseId(@RequestParam UUID courseId){
        try {
            List<ResponseAssignment> deadlinePassedAssignmentsByCourseId = assignmentService.getDeadlinePassedAssignmentsByCourseId(courseId);
            return ResponseEntity.ok(deadlinePassedAssignmentsByCourseId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/getAssignmentsSubmittedByAllStudent")
    public ResponseEntity<?> getAssignmentsSubmittedByAllStudent(@RequestParam UUID courseId){
        try {
            List<AssignmentSubmissionStats> assignmentsSubmittedByAllStudents = assignmentService.getAssignmentsSubmittedByAllStudents(courseId);
            return ResponseEntity.ok(assignmentsSubmittedByAllStudents);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/getAssignmentByAssignmentId")
    public ResponseEntity<?> getAssignmentByAssignmentId(@RequestParam UUID assignmentId){
        try {
            ResponseAssignment responseAssignment = assignmentService.getAssignmentByAssignmentId(assignmentId);
            return ResponseEntity.ok(responseAssignment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
