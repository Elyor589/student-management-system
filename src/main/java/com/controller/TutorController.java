package com.controller;

import com.dto.TutorDto;
import com.service.TutorService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
