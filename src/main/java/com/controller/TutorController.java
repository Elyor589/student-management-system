package com.controller;

import com.dto.TutorDto;
import com.dto.stats.AssignmentStatsResponse;
import com.service.TutorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/tutors")
public class TutorController {
    private final TutorService tutorService;

    public TutorController(TutorService tutorService) {
        this.tutorService = tutorService;
    }

    @PostMapping("/create-tutor")
    public TutorDto createTutor(@RequestBody TutorDto tutorDto) {
        return tutorService.createTutor(tutorDto);
    }

    @GetMapping("/getAllTutors")
    public ResponseEntity<List<TutorDto>> getAllTutors() {
        return ResponseEntity.ok(tutorService.getAllTutors());
    }

    @GetMapping("/getTutorByTutorId")
    public ResponseEntity<?> getTutorByTutorId(@RequestParam("tutorId") String tutorId) {
        try {
            TutorDto tutorByTutorId = tutorService.getTutorByTutorId(tutorId);
            return ResponseEntity.status(HttpStatus.OK).body(tutorByTutorId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/getAssignmentStatsByCourseId")
    public ResponseEntity<?> getAssignmentStatsByCourseId(@RequestParam UUID courseId) {
        try {
            AssignmentStatsResponse assignmentStatsByCourseId = tutorService.getAssignmentStatsByCourseId(courseId);
            return ResponseEntity.ok(assignmentStatsByCourseId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
