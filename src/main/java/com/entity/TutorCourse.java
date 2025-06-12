package com.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "tutor_courses", schema = "student_management")
public class TutorCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID TutorCourseId;

    @ManyToOne
    @JoinColumn(name = "tutor_id", nullable = false)
    private Tutor tutor;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    private String semester;
    private Integer year;
}
