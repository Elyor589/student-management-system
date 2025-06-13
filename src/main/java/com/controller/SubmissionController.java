package com.controller;

import com.dto.submission.RequestSubmissionDto;
import com.dto.submission.RequestSubmitAssignment;
import com.dto.submission.ResponseSubmissionDto;
import com.dto.submission.ResponseSubmitAssignment;
import com.service.SubmissionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/submissions")
public class SubmissionController {
    private final SubmissionService submissionService;

    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @GetMapping("/getAllSubmissionByTutorId")
    public ResponseEntity<List<?>> getAllSubmissionByTutorId(@RequestParam("tutorId") String tutorId) {
        try {
            List<ResponseSubmitAssignment> response = submissionService.getAllSubmissionsByTutorId(tutorId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/grade-submission")
    public ResponseEntity<?> gradeSubmission(@RequestParam String tutorId, @RequestParam UUID submissionId, @RequestBody RequestSubmissionDto requestSubmissionDto){
        try {
            ResponseSubmissionDto response = submissionService.gradeSubmission(tutorId, submissionId, requestSubmissionDto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/submit-assignment")
    public ResponseEntity<?> submitAssignment(@RequestParam String studentId, @RequestParam UUID assignmentId, @RequestBody RequestSubmitAssignment requestSubmitAssignment){
        try {
            ResponseSubmitAssignment submitAssignment = submissionService.submitAssignment(studentId, assignmentId, requestSubmitAssignment);
            return ResponseEntity.ok(submitAssignment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
