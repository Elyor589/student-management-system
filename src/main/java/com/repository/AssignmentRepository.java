package com.repository;

import com.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, UUID> {
    Optional<Assignment> findByAssignmentId(UUID assignmentId);

    @Query("SELECT a FROM Assignment a WHERE a.tutor.tutorId = :tutorId AND a.course.courseId = :courseId")
    List<Assignment> findByTutorIdAndCourseId(String tutorId, UUID courseId);

    List<Assignment> findByCourseCourseId(UUID courseId);

    @Query("SELECT a FROM Assignment a WHERE a.dueDate < CURRENT_TIMESTAMP")
    List<Assignment> findByDueDatePassedAssignments();
}
