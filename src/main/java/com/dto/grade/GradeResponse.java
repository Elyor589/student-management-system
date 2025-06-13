package com.dto.grade;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class GradeResponse {
    private BigDecimal finalPercentage;
    private String letterGrade;
}
