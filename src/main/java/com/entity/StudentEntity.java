package com.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
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

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

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

    @Column(name = "code")
    private String code;

    @Column(name = "code_expiry")
    private LocalDateTime codeExpiry;

    @Column(name = "code_used")
    private Boolean codeUsed = false;

    @Column(name = "role")
    private String role;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Enrollment> enrollments = new ArrayList<>();

    public Boolean getCodeUsed() {
        if (codeUsed == null){
            return false;
        }
        return codeUsed;
    }

    @PrePersist
    public void generateId() {
        this.studentId = generateStudentId();
    }

    public String generateStudentId() {
        String letters = getRandomLetters(2);
        String numbers = String.format("%05d", (int)(Math.random()*100000));
        return letters + numbers;
    }

    public String getRandomLetters(int length) {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(alphabet.charAt((int) (Math.random()*alphabet.length())));
        }
        return sb.toString();
    }
}
