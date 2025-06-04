package com.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "students", schema = "student_management")
public class StudentEntity {
    @Id
    private String studentId;

    @NotBlank(message = "First name is mandatory")
    @Column(name = "first_name")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    @Column(name = "last_name")
    private String lastName;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is mandatory")
    @Column(unique = true)
    private String email;

    @Past(message = "Date of birth must be in the past")
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Min(value = 16, message = "Age should be at least 16")
    private Integer age;

    @NotBlank(message = "Gender is mandatory")
    private String gender;

    @NotBlank(message = "Phone number is mandatory")
    @Column(name = "phone_number", unique = true)
    private String phoneNumber;


    @PrePersist
    public void generateId() {
        this.studentId = generateStudentId();
    }

    private String generateStudentId() {
        String letters = getRandomLetters(2);
        String numbers = String.format("%05d", (int)(Math.random()*100000));
        return letters + numbers;
    }

    private String getRandomLetters(int length) {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(alphabet.charAt((int) (Math.random()*alphabet.length())));
        }
        return sb.toString();
    }
}
