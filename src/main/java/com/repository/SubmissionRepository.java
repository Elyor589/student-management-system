package com.repository;

import com.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, UUID> {
    Optional<Submission> findBySubmissionId(UUID submissionId);

    @Query("SELECT s FROM Submission s WHERE s.student.studentId = :studentId AND s.assignment.course.courseId = :courseId")
    List<Submission> findByStudentIdAndCourseId(String studentId, UUID courseId);

    List<Submission> findByStudentStudentId(String studentStudentId);
}
