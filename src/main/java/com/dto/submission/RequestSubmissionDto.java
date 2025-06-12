package com.dto.submission;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RequestSubmissionDto {
    private BigDecimal score;
    private String comment;
}
