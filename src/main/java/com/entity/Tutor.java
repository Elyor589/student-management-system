package com.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "tutors", schema = "student_management")
public class Tutor {
    @Id
    private String tutorId;

    @Column(unique = true , nullable = false)
    private String username;

    @Column(unique = true , nullable = false)
    private String password;

    @Column(nullable = true)
    private String firstName;

    @Column(nullable = true)
    private String lastName;

    @Column(nullable = true)
    private LocalDate birthDate;

    @Column(unique = true, nullable = true)
    private String email;

    @Column(nullable = true)
    private String phoneNumber;

    @Column(nullable = true)
    private LocalDateTime createdAt;

    @Column(nullable = true)
    private Boolean isActive;

    @Column(nullable = true)
    private String department;

    @Column(name = "role")
    private String role;

    @OneToMany(mappedBy = "tutor")
    private List<TutorCourse> tutorCourses = new ArrayList<>();

    @OneToMany(mappedBy = "tutor")
    private List<Assignment> assignments = new ArrayList<>();

    @PrePersist
    public void generateTutorId() {
        StudentEntity student = new StudentEntity();
        this.tutorId = student.generateStudentId();
    }
}
