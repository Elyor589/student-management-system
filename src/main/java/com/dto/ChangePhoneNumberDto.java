package com.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChangePhoneNumberDto {
    private String code;
    private String phoneNumber;
    private String newPhoneNumber;
    private LocalDateTime codeExpiry;
    private boolean codeUsed;

}
