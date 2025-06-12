package com.dto.assignment;

import com.dto.CourseDto;
import com.dto.TutorDto;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ResponseAssignment {
    private String title;
    private String description;
    private BigDecimal maxScore;
    private LocalDateTime dueDate;
    private LocalDateTime createdAt;
    private CourseDto course;
    private TutorDto tutor;
//    private List<Submission> submissions;
}
