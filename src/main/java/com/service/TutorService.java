package com.service;

import com.dto.TutorDto;
import com.entity.Tutor;
import com.repository.TutorRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TutorService {
    private final TutorRepository tutorRepository;
    private final StudentService studentService;
    private final PasswordEncoder passwordEncoder;

    public TutorDto createTutor(TutorDto tutorDto) {
        Tutor tutor = new Tutor();

        Optional<Tutor> tutorOptional = tutorRepository.findByTutorId(tutor.getTutorId());
        if (tutorOptional.isPresent()) {
            throw new RuntimeException("Tutor already exists");
        }

        boolean validPhoneNumber = studentService.phoneNumberCheck(tutorDto.getPhoneNumber());
        if (!validPhoneNumber) {
            throw new RuntimeException("Invalid phone number");
        }

        tutor.setFirstName(tutorDto.getFirstName());
        tutor.setLastName(tutorDto.getLastName());
        tutor.setEmail(tutorDto.getEmail());
        tutor.setPhoneNumber(tutorDto.getPhoneNumber());
        tutor.setBirthDate(tutorDto.getBirthDate());
        tutor.setIsActive(true);
        tutor.setUsername(tutorDto.getUsername());
        tutor.setPassword(passwordEncoder.encode(tutorDto.getPassword()));
        tutor.setCreatedAt(LocalDateTime.now());
        tutor.setDepartment(tutorDto.getDepartment());
        tutor.setRole("tutor");
        tutorRepository.save(tutor);
        return tutorDto;
    }
}
