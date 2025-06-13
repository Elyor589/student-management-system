package com.dto.submission;

import com.dto.assignment.AssignmentDto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ResponseSubmitAssignment {
    private UUID submissionId;
    private UUID assignmentId;
    private String studentId;
    private String studentName;
    private LocalDateTime submitDate;
    private String fileName;
    private String comment;
    private boolean graded;
    private AssignmentDto assignment;
}
