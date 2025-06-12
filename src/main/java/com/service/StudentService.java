package com.service;

import com.dto.DtoMapper;
import com.dto.RequestCreateStudentDto;
import com.dto.StudentDto;
import com.entity.StudentEntity;
import com.repository.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
@AllArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public List<StudentDto> getAllStudents() {
        List<StudentEntity> studentEntities = studentRepository.findAll();
        return studentEntities.stream().map(DtoMapper::studentDto).toList();
    }

    public Optional<StudentDto> getStudentByStudentId(String studentId) {
        return studentRepository.findByStudentId(studentId)
                .map(DtoMapper::studentDto);
    }

    public StudentEntity createStudent(RequestCreateStudentDto requestCreateStudentDto) {
       StudentEntity student = new StudentEntity();
       String phoneNumber = requestCreateStudentDto.getPhoneNumber();
       String email = requestCreateStudentDto.getEmail();

       boolean valid = phoneNumberCheck(phoneNumber);

       if (!valid) {
           throw new RuntimeException("Invalid phone number");
       }

       Optional<StudentEntity> students = studentRepository.findByPhoneNumber(phoneNumber);
       Optional<StudentEntity> existingStudentByEmail = studentRepository.findByEmail(email);

       if (students.isPresent()) {
           throw new RuntimeException("Student with this phone number already exists");
       }

       if (existingStudentByEmail.isPresent()) {
           throw new RuntimeException("Student with this email already exists");
       }

       String gender = requestCreateStudentDto.getGender().toString();
       student.setFirstName(requestCreateStudentDto.getFirstName());
       student.setLastName(requestCreateStudentDto.getLastName());
       student.setUsername(requestCreateStudentDto.getUsername());
       student.setPassword(passwordEncoder.encode(requestCreateStudentDto.getPassword()));
       student.setEmail(requestCreateStudentDto.getEmail());
       student.setDateOfBirth(requestCreateStudentDto.getDateOfBirth());
       student.setAge(requestCreateStudentDto.getAge());
       student.setGender(gender);
       student.setPhoneNumber(requestCreateStudentDto.getPhoneNumber());
       student.setRole("student");
       return studentRepository.save(student);
    }

    @Transactional
    public Optional<StudentEntity> updateStudent(String studentId, RequestCreateStudentDto dto) {
        StudentEntity student = studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new RuntimeException("Student Id not found"));

        Optional<StudentEntity> existingByEmail = studentRepository.findByEmail(dto.getEmail());
        if (existingByEmail.isPresent() && !existingByEmail.get().getEmail().equals(student.getEmail())) {
            throw new RuntimeException("Student with this email already exists");
        }

        Optional<StudentEntity> students = studentRepository.findByPhoneNumber(dto.getPhoneNumber());
        if (students.isPresent() && !students.get().getEmail().equals(student.getEmail())) {
            throw new RuntimeException("Student with this phone number already exists");
        }

        String gender = dto.getGender().toString();
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setEmail(dto.getEmail());
        student.setPhoneNumber(dto.getPhoneNumber());
        student.setAge(dto.getAge());
        student.setDateOfBirth(dto.getDateOfBirth());
        student.setGender(gender);

        try {
            return Optional.of(studentRepository.save(student));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save student: " + e.getMessage());
        }
    }

    public String changePhoneNumber(String phoneNumber) {
        Optional<StudentEntity> optionalStudent = studentRepository.findByPhoneNumber(phoneNumber);
        if (!optionalStudent.isPresent()) {
            throw new RuntimeException("Student with this phone number does not exist");
        }

        StudentEntity student = optionalStudent.get();
        String email = student.getEmail();

        String code = String.valueOf(ThreadLocalRandom.current().nextLong(100000, 1000000));

        student.setCode(code);
        student.setCodeUsed(false);
        student.setCodeExpiry(LocalDateTime.now().plusMinutes(10));
        studentRepository.save(student);

        String content = "<p>Use this code to confirm phone number change <b>" + code + "<b/></p>";
        String subject = "Confirm Your Phone Number Change";
        emailService.sendConfirmationEmail(email, subject, content);

        return "Code sent to " + email;
    }

    @Transactional
    public String confirmCode(String code, String newPhoneNumber) {
        StudentEntity student = studentRepository.findByCode(code).orElseThrow(() ->
                new RuntimeException("Code is invalid"));

        if (student.getCodeExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Code expiry is invalid");
        }

        if (student.getCodeUsed()){
            throw new RuntimeException("This code is used");
        }

        Optional<StudentEntity> optionalStudent = studentRepository.findByPhoneNumber(newPhoneNumber);
        if (optionalStudent.isPresent()) {
            throw new RuntimeException("Student with this phone number already exists");
        }

        student.setPhoneNumber(newPhoneNumber);
        student.setCodeUsed(true);
        studentRepository.save(student);
        return "Phone number changed";
    }

    public boolean deleteStudent(String studentId) {
        if (studentRepository.existsById(studentId)) {
            studentRepository.deleteById(studentId);
            return true;
        }

        return false;
    }

    public boolean phoneNumberCheck(String phoneNumber) {
        return phoneNumber != null && phoneNumber.matches("^\\+998\\d{9}$");
    }

}
