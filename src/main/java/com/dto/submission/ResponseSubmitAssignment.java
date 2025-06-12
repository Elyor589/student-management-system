package com.dto.submission;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ResponseSubmitAssignment {
    private UUID submissionId;
    private UUID assignmentId;
    private String assignmentTitle;
    private String studentId;
    private String studentName;
    private LocalDateTime submitDate;
    private String fileName;
    private BigDecimal maxScore;
    private String comment;
}
