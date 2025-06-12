package com.service;

import com.dto.CourseDto;
import com.dto.DtoMapper;
import com.entity.Course;
import com.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CourseService {
    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<CourseDto> getAllCourses() {
        List<Course> courseList = courseRepository.findAll();
        return courseList.stream().map(DtoMapper::courseDto).collect(Collectors.toList());
    }

    public Course createCourse(String title) {
        return courseRepository.findByTitle(title).orElseGet(() -> {
            Course newCourse = new Course();
            newCourse.setCourseId(UUID.randomUUID());
            newCourse.setTitle(title);
            courseRepository.save(newCourse);
            return newCourse;
        });
    }

    public boolean deleteCourse(UUID courseId) {
        if (courseRepository.existsById(courseId)){
            courseRepository.deleteById(courseId);
            return true;
        }
        return false;
    }
}
