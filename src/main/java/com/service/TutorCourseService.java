package com.service;

import com.dto.TutorCourseRequestDto;
import com.dto.TutorCourseResponse;
import com.entity.Course;
import com.entity.Tutor;
import com.entity.TutorCourse;
import com.repository.CourseRepository;
import com.repository.TutorCourseRepository;
import com.repository.TutorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class TutorCourseService {
    private final TutorCourseRepository tutorCourseRepository;
    private final CourseRepository courseRepository;
    private final TutorRepository tutorRepository;

    public TutorCourseService(TutorCourseRepository tutorCourseRepository,
                              CourseRepository courseRepository,
                              TutorRepository tutorRepository) {
        this.tutorCourseRepository = tutorCourseRepository;
        this.courseRepository = courseRepository;
        this.tutorRepository = tutorRepository;
    }

    @Transactional
    public TutorCourseResponse assignTutorToCourse(UUID courseId, String tutorId, TutorCourseRequestDto request) {
        Course course = courseRepository.findByCourseId(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Tutor tutor = tutorRepository.findByTutorId(tutorId)
                .orElseThrow(() -> new RuntimeException("Tutor not found"));

        Optional<TutorCourse> optionalTutorCourse = tutorCourseRepository.findByCourseCourseId(courseId);
        if (optionalTutorCourse.isPresent()) {
            throw new RuntimeException("Tutor already assigned to course");
        }

        TutorCourse tutorCourse = new TutorCourse();
        tutorCourse.setCourse(course);
        tutorCourse.setTutor(tutor);
        tutorCourse.setSemester(request.getSemester());
        tutorCourse.setYear(request.getYear());
        tutorCourseRepository.save(tutorCourse);
        return new TutorCourseResponse(
                tutorCourse.getTutorCourseId(),
                tutorCourse.getSemester(),
                tutorCourse.getYear(),
                course.getCourseId(),
                course.getTitle(),
                tutor.getFirstName(),
                tutor.getTutorId()
        );
    }
}
