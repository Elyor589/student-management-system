package com.dto.submission;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestSubmitAssignment {
    private UUID assignmentId;
    private String fileName;
    private Long fileSize;
    private String fileType;
    private String comment;
}
