package com.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CourseDto {
    private UUID courseId;
    private String courseTitle;
    private List<TutorCourseResponse> tutorCourses;
    private List<EnrollmentDto> enrollments;
}
