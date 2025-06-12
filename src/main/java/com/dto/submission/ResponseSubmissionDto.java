package com.dto.submission;

import com.dto.StudentDto;
import com.dto.TutorDto;
import com.dto.assignment.AssignmentDto;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ResponseSubmissionDto {
    private AssignmentDto assignment;
    private StudentDto student;
    private TutorDto tutor;
    private LocalDateTime submitDate;
    private String comment;
    private BigDecimal score;
    private boolean isGraded;
}
