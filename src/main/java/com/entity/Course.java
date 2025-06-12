package com.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(schema = "student_management")
public class Course {
    @Id
    @Column(name = "course_id")
    private UUID courseId;

    private String title;

    @OneToMany(mappedBy = "course")
    private List<TutorCourse> tutorCourses = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Enrollment> enrollments = new ArrayList<>();
}
