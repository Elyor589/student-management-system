package com.repository;

import com.dto.assignment.AssignmentSubmissionStats;
import com.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("""
        select
            a.assignmentId,
            a.title,
            count(distinct s.student.studentId) as completed,
            count(distinct e.student.studentId) as enrolled
        from Assignment a
        join Course c on a.course.courseId = c.courseId
        left join Enrollment e on e.course.courseId = a.course.courseId
        left join Submission s on s.assignment.assignmentId = a.assignmentId
        and s.student.studentId= e.student.studentId
        where a.course.courseId = :courseId
        group by a.assignmentId, a.title
        having count(distinct s.student.studentId) = count(distinct e.student.studentId)""")
    List<AssignmentSubmissionStats> findAssignmentSubmittedByAllStudents(@Param("courseId") UUID courseId);
}
