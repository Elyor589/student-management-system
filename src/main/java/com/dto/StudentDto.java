package com.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class StudentDto {
    private String studentId;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dateOfBirth;
    private Integer age;
    private String gender;
    private String phoneNumber;
    private List<EnrollmentDto> enrollments;
}
