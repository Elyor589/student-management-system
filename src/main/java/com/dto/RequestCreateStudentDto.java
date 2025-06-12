package com.dto;

import com.enums.GenderEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestCreateStudentDto {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    private Integer age;
    private GenderEnum gender;
    private String phoneNumber;
}
