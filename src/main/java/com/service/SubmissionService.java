package com.service;

import com.dto.TutorDto;
import com.dto.submission.RequestSubmissionDto;
import com.dto.submission.RequestSubmitAssignment;
import com.dto.submission.ResponseSubmissionDto;
import com.dto.submission.ResponseSubmitAssignment;
import com.entity.Assignment;
import com.entity.StudentEntity;
import com.entity.Submission;
import com.entity.Tutor;
import com.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class SubmissionService {
    private final TutorRepository tutorRepository;
    private final SubmissionRepository submissionRepository;
    private final AssignmentService assignmentService;
    private final StudentRepository studentRepository;
    private final AssignmentRepository assignmentRepository;
    private final EnrollmentRepository enrollmentRepository;

    public SubmissionService(TutorRepository tutorRepository,
                             SubmissionRepository submissionRepository,
                             AssignmentService assignmentService,
                             StudentRepository studentRepository,
                             AssignmentRepository assignmentRepository,
                             EnrollmentRepository enrollmentRepository) {
        this.tutorRepository = tutorRepository;
        this.submissionRepository = submissionRepository;
        this.assignmentService = assignmentService;
        this.studentRepository = studentRepository;
        this.assignmentRepository = assignmentRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    public ResponseSubmissionDto gradeSubmission(String tutorId, UUID submissionId, RequestSubmissionDto request) {
        Optional<Tutor> optionalTutor = tutorRepository.findByTutorId(tutorId);
        if (!optionalTutor.isPresent()) {
            throw new RuntimeException("Tutor not found");
        }

        Tutor tutor = optionalTutor.get();
        Submission submission = getSubmission(tutorId, request, submissionId);

        submission.setScore(request.getScore());
        submission.setComments(request.getComment());
        submission.setGraded(true);
        submission.setGradedDate(LocalDateTime.now());
        submission.setSubmissionDate(LocalDateTime.now());
        submission.setTutor(tutor);
        
        Submission gradedSubmission = submissionRepository.save(submission);
        return convertSubmissionToResponseSubmissionDto(gradedSubmission);
    }


    public ResponseSubmitAssignment submitAssignment(String studentId, String filePath, RequestSubmitAssignment request) {
        Optional<StudentEntity> optionalStudent = studentRepository.findByStudentId(studentId);
        if (!optionalStudent.isPresent()) {
            throw new RuntimeException("Student not found");
        }
        Optional<Assignment> assignmentOptional = assignmentRepository.findByAssignmentId(request.getAssignmentId());
        if (!assignmentOptional.isPresent()) {
            throw new RuntimeException("Assignment not found");
        }
        Assignment assignment = assignmentOptional.get();
        if (LocalDateTime.now().isAfter(assignment.getDueDate())){
            throw new RuntimeException("Submission deadline has passed");
        }

        StudentEntity student = optionalStudent.get();

        if (!enrollmentRepository.existsByStudentIdAndCourseId(student.getStudentId(), request.getAssignmentId())) {
            throw new RuntimeException("Student is not enrolled in this course");
        }

        Submission submission = new Submission();
        submission.setAssignment(assignment);
        submission.setStudent(student);
        submission.setTutor(assignment.getTutor());
        submission.setFilePath(filePath);
        submission.setComments(request.getComment());
        submission.setSubmissionDate(LocalDateTime.now());

        Submission gradedSubmission = submissionRepository.save(submission);
        return mapToSubmissionResponse(gradedSubmission);
    }

    private ResponseSubmitAssignment mapToSubmissionResponse(Submission submission) {
        ResponseSubmitAssignment response = new ResponseSubmitAssignment();
        response.setAssignmentId(submission.getAssignment().getAssignmentId());
        response.setStudentId(submission.getStudent().getStudentId());
        response.setSubmissionId(submission.getSubmissionId());
        response.setAssignmentTitle(submission.getAssignment().getTitle());
        response.setStudentName(submission.getStudent().getFirstName());
        response.setSubmitDate(submission.getSubmissionDate());
        response.setFileName(submission.getFilePath());
        response.setComment(submission.getComments());
        response.setMaxScore(submission.getScore());
        return response;
    }



    private Submission getSubmission(String tutorId, RequestSubmissionDto request, UUID submissionId) {
        Optional<Submission> submissionOptional = submissionRepository.findBySubmissionId(submissionId);
        if (!submissionOptional.isPresent()) {
            throw new RuntimeException("Submission not found");
        }

        Submission submission = submissionOptional.get();

        if (!submission.getTutor().getTutorId().equals(tutorId)){
            throw new RuntimeException("Tutor does not belong to this submission");
        }

        if (submission.getScore() != null && submission.isGraded()){
            throw new RuntimeException("Submission is already graded");
        }

        if (request.getScore().compareTo(submission.getAssignment().getMaxScore()) > 0){
            throw new RuntimeException("Score is greater than max score");
        }
        return submission;
    }

    private ResponseSubmissionDto convertSubmissionToResponseSubmissionDto(Submission submission) {
        ResponseSubmissionDto dto = new ResponseSubmissionDto();
        dto.setScore(submission.getScore());
        dto.setComment(submission.getComments());
        dto.setGraded(submission.isGraded());
        dto.setSubmitDate(submission.getSubmissionDate());
        TutorDto tutorDto = assignmentService.convertToTutorDto(submission.getTutor());
        dto.setTutor(tutorDto);

        return dto;
    }
}
