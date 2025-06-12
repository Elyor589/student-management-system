package com.controller;

import com.dto.RequestCreateStudentDto;
import com.dto.StudentDto;
import com.entity.StudentEntity;
import com.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/v1/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/")
    public String home() {
        return "Backend is running";
    }

    @GetMapping("/getAllStudents")
    public ResponseEntity<List<StudentDto>> getAllStudents() {
        return new ResponseEntity<>(studentService.getAllStudents(), HttpStatus.OK);
    }

    @GetMapping("getStudent/{id}")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable String id) {
        return studentService.getStudentByStudentId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping("/create-student")
    public ResponseEntity<?> createStudent(@Valid @RequestBody RequestCreateStudentDto dto) {
        try {
            StudentEntity createdStudent = studentService.createStudent(dto);
            return ResponseEntity.ok(createdStudent);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PutMapping("update-student/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable String id, @Valid @RequestBody RequestCreateStudentDto dto) {
        try {
            Optional<StudentEntity> updateStudent = studentService.updateStudent(id, dto);
            if (!updateStudent.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found");
            }

            return ResponseEntity.ok(updateStudent.get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/change-phone-number")
    public ResponseEntity<?> changePhoneNumber(@RequestParam String phoneNumber) {
        try {
            String changePhoneNumber = studentService.changePhoneNumber(phoneNumber);
            return ResponseEntity.ok(changePhoneNumber);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/code-confirm")
    public ResponseEntity<?> confirmCode(@RequestParam String code, @RequestParam String newPhoneNumber) {
        try {
            String confirmCode = studentService.confirmCode(code, newPhoneNumber);
            return ResponseEntity.ok(confirmCode);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("delete-student/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable String id) {
        try {
            boolean deleted = studentService.deleteStudent(id);
            if (deleted) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
