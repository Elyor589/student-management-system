package com.service;

import com.dto.grade.GradeResponse;
import com.entity.Assignment;
import com.entity.Enrollment;
import com.entity.StudentEntity;
import com.entity.Submission;
import com.repository.AssignmentRepository;
import com.repository.EnrollmentRepository;
import com.repository.StudentRepository;
import com.repository.SubmissionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GradeService {
    private final AssignmentRepository assignmentRepository;
    private final SubmissionRepository submissionRepository;
    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;

    public GradeService(AssignmentRepository assignmentRepository,
                        SubmissionRepository submissionRepository,
                        StudentRepository studentRepository,
                        EnrollmentRepository enrollmentRepository) {
        this.assignmentRepository = assignmentRepository;
        this.submissionRepository = submissionRepository;
        this.studentRepository = studentRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    public GradeResponse calculateFinalGrade(String tutorId, UUID courseId, String studentId) {

        Optional<StudentEntity> optionalStudent = studentRepository.findById(studentId);
        if (!optionalStudent.isPresent()) {
            throw new RuntimeException("Student not found");
        }

        Optional<Enrollment> enrollmentOptional = enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId);
        if (!enrollmentOptional.isPresent()) {
            throw new RuntimeException("Enrollment not found");
        }

        List<Assignment> assignmentList = assignmentRepository.findByTutorIdAndCourseId(tutorId, courseId);
        List<Submission> submissionList = submissionRepository.findByStudentIdAndCourseId(studentId, courseId);
        BigDecimal totalScore = BigDecimal.ZERO;
        BigDecimal totalMaxScore = BigDecimal.ZERO;

        for (Assignment assignment : assignmentList) {
            Optional<Submission> submission = submissionList.stream()
                    .filter(s -> s.getAssignment().getAssignmentId().equals(assignment.getAssignmentId()))
                    .findFirst();

            if (submission.isPresent() && submission.get().isGraded()) {
                totalScore = totalScore.add(submission.get().getScore());
                totalMaxScore = totalMaxScore.add(assignment.getMaxScore());
            }
        }

        BigDecimal finalPercentage = BigDecimal.ZERO;
        if (totalScore.compareTo(BigDecimal.ZERO) > 0) {
            finalPercentage = totalScore.divide(totalMaxScore, 2, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }

        String letterGrade = calculateLetterGrade(finalPercentage);

        Enrollment enrollment = enrollmentOptional.get();
        enrollment.setGrade(letterGrade);
        enrollment.setFinalPercentage(finalPercentage);
        enrollmentRepository.save(enrollment);

        return new GradeResponse(finalPercentage, letterGrade);
    }

    private String calculateLetterGrade(BigDecimal percentage) {
        double percent = percentage.doubleValue();
        if (percent >= 90) {
            return "A";
        } else if (percent >= 80) {
            return "B";
        } else if (percent >= 70) {
            return "C";
        } else if (percent >= 60) {
            return "D";
        }
        return "F";
    }
}
