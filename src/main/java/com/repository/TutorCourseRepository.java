package com.repository;

import com.entity.TutorCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TutorCourseRepository extends JpaRepository<TutorCourse, UUID> {
    Optional<TutorCourse> findByCourseCourseId(UUID courseId);
}
