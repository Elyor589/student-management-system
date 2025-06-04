package com.service;

import com.dto.RequestCreateStudentDto;
import com.entity.StudentEntity;
import com.repository.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;

    public List<StudentEntity> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<StudentEntity> getStudentByStudentId(String studentId) {
        return studentRepository.findByStudentId(studentId);
    }

    public StudentEntity createStudent(RequestCreateStudentDto requestCreateStudentDto) {
       StudentEntity student = new StudentEntity();
       String phoneNumber = requestCreateStudentDto.getPhoneNumber();
       String email = requestCreateStudentDto.getEmail();
       Optional<StudentEntity> students = studentRepository.findByPhoneNumber(phoneNumber);
       Optional<StudentEntity> existingStudentByEmail = studentRepository.findByEmail(email);

       if (students.isPresent()) {
           throw new RuntimeException("Student with this phone number already exists");
       }

       if (existingStudentByEmail.isPresent()) {
           throw new RuntimeException("Student with this email already exists");
       }


       student.setFirstName(requestCreateStudentDto.getFirstName());
       student.setLastName(requestCreateStudentDto.getLastName());
       student.setEmail(requestCreateStudentDto.getEmail());
       student.setDateOfBirth(requestCreateStudentDto.getDateOfBirth());
       student.setAge(requestCreateStudentDto.getAge());
       student.setGender(requestCreateStudentDto.getGender());
       student.setPhoneNumber(requestCreateStudentDto.getPhoneNumber());
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

        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setEmail(dto.getEmail());
        student.setPhoneNumber(dto.getPhoneNumber());
        student.setAge(dto.getAge());
        student.setDateOfBirth(dto.getDateOfBirth());
        student.setGender(dto.getGender());

        try {
            return Optional.of(studentRepository.save(student));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save student: " + e.getMessage());
        }
    }


    public boolean deleteStudent(String studentId) {
        if (studentRepository.existsById(studentId)) {
            studentRepository.deleteById(studentId);
            return true;
        }

        return false;
    }

}
