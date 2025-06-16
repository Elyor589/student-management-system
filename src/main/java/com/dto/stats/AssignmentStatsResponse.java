package com.dto.stats;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AssignmentStatsResponse {
    private Long totalAssignments;
    private Long totalEnrollments;
    private Long totalSubmissions;
    private Long totalNoSubmissions;
}
