package com.dto.submission;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestSubmitAssignment {
    private String filePath;
    private String comment;
}
