package com.repository;

import com.dto.submission.ResponseSubmitAssignmentInterface;
import com.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    Long countByAssignmentAssignmentId(UUID assignmentId);

    @Query("select s.studentId as studentId, s.firstName as studentFirstName, s.lastName as studentLastName " +
            "from StudentEntity s " +
            "where s.studentId in (select e.student.studentId " +
            "                       from Enrollment e) and s.studentId not in (select s2.student.studentId " +
            "                                                                                      from Submission s2" +
            "                                                                                      where s2.assignment.assignmentId = :assignmentId)")
    List<ResponseSubmitAssignmentInterface> findNotSubmittedSubmissionsByAssignmentId(@Param("assignmentId") UUID assignmentId);

}
