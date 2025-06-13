package com.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class EnrollmentDto {
    private UUID enrollmentId;
    private String courseTitle;
    private LocalDate enrollmentDate;
    private String grade;
    private String status;
    private StudentDto student;
}
