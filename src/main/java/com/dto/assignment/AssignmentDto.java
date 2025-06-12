package com.dto.assignment;

import com.dto.CourseDto;
import com.dto.TutorDto;
import com.entity.Submission;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AssignmentDto {
    private String assignmentId;
    private String title;
    private String description;
    private BigDecimal maxScore;
    private LocalDateTime dueDate;
    private CourseDto course;
    private TutorDto tutor;
//    private List<Submission> submissions;
}
