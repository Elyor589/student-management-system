package com.dto;

import java.util.UUID;

public record TutorCourseResponse(UUID tutorCourseId,
                                  String semester,
                                  Integer year,
                                  UUID courseId,
                                  String courseName,
                                  String tutorName,
                                  String tutorId) {
}
