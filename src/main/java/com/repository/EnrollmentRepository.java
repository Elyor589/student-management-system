package com.repository;

import com.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EnrollmentRepository extends JpaRepository<Enrollment, UUID> {

    @Query("SELECT e FROM Enrollment e WHERE e.student.studentId = :studentId")
    List<Enrollment> findByStudentId(@Param("studentId") String studentId);

    @Query("SELECT e FROM Enrollment e WHERE e.course.courseId = :courseId")
    List<Enrollment> findByCourseId(@Param("courseId") UUID courseId);

    @Query("SELECT COUNT(e) > 0 FROM Enrollment e WHERE e.student.studentId = :studentId AND e.course.courseId = :courseId")
    boolean existsByStudentIdAndCourseId(@Param("studentId") String studentId, @Param("courseId") UUID courseId);

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Enrollment e WHERE e.student.studentId = :studentId AND e.course.courseId = " +
            "(SELECT a.course.courseId FROM Assignment a WHERE a.assignmentId = :assignmentId) ")
    boolean isStudentEnrolledInCourseWithAssignment(String studentId, UUID assignmentId);

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Submission e WHERE e.student.studentId = :studentId AND e.assignment.assignmentId = :assignmentId")
    boolean existsBySubmissionIdAndAssignmentId(String studentId, UUID assignmentId);

    @Query("SELECT e FROM Enrollment e WHERE e.student.studentId = :studentId AND e.course.courseId = :courseId")
    Optional<Enrollment> findByStudentIdAndCourseId(@Param("studentId") String studentId, @Param("courseId") UUID courseId);
}
