package com.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class EnrollmentDto {
    private UUID enrollmentId;
    private String courseTitle;
    private LocalDate enrollmentDate;
    private String grade;
    private String status;
}
