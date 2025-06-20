package com.dto.assignment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentSubmissionStats {
    private UUID assignmentId;
    private String title;
    private Long completed;
    private Long enrolled;
}
