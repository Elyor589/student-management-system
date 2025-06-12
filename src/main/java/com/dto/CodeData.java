package com.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CodeData {
    private String phoneNumber;
    private LocalDateTime codeExpiry;
}
