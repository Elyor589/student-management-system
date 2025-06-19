package com.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {
    private UUID registerId;
    private String username;
    private String password;
    private String phoneNumber;
    private String email;
}
