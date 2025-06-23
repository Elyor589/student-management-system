package com.service;

import com.dto.CourseDto;
import com.dto.TutorDto;
import com.dto.assignment.AssignmentSubmissionStats;
import com.dto.assignment.RequestAssignment;
import com.dto.assignment.ResponseAssignment;
import com.entity.*;
import com.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AssignmentService {

    private final TutorRepository tutorRepository;
    private final AssignmentRepository assignmentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final EmailService emailService;
    private final SubmissionRepository submissionRepository;

    public AssignmentService(TutorRepository tutorRepository,
                             AssignmentRepository assignmentRepository,
                             CourseRepository courseRepository, EnrollmentRepository enrollmentRepository, StudentRepository studentRepository, EmailService emailService, SubmissionRepository submissionRepository) {
        this.tutorRepository = tutorRepository;
        this.assignmentRepository = assignmentRepository;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.emailService = emailService;
        this.submissionRepository = submissionRepository;
    }


    public ResponseAssignment createAssignment(String tutorId, UUID courseId, RequestAssignment request) {
        Optional<Tutor> tutorOptional = tutorRepository.findByTutorId(tutorId);
        if (!tutorOptional.isPresent()) {
            throw new RuntimeException("Tutor not found");
        }

        Optional<Course> optionalCourse = courseRepository.findByCourseId(courseId);
        if (!optionalCourse.isPresent()) {
            throw new RuntimeException("Course not found");
        }

        Course course = optionalCourse.get();
        Tutor tutor = tutorOptional.get();

        Assignment assignment = new Assignment();
        assignment.setTitle(request.getTitle());
        assignment.setDescription(request.getDescription());
        assignment.setCreatedAt(LocalDateTime.now());
        assignment.setDueDate(request.getDueDate());
        assignment.setMaxScore(request.getMaxScore());
        assignment.setCourse(course);
        assignment.setTutor(tutor);

        Assignment savedAssignment = assignmentRepository.save(assignment);
        List<Enrollment> enrollments = course.getEnrollments();
        for (Enrollment enrollment : enrollments) {
            StudentEntity student = enrollment.getStudent();
            String email = student.getEmail();
            String subject = "New Assignment " + savedAssignment.getTitle();
            String content = "<p>New assignment details<p>"
                    + savedAssignment.getDescription() + "</p>"
                    + "<p>Due date: " + savedAssignment.getDueDate() + "</p>"
                    + "<p> Max score for this project: " + savedAssignment.getMaxScore() + "</p>"
                    + "<p> Course name: " + savedAssignment.getCourse().getTitle() + "</p>"
                    + "<p> If you have any questions contact me " + savedAssignment.getTutor().getEmail() + "</p>"
                    + "<p> " + savedAssignment.getTutor().getFirstName() + " " + savedAssignment.getTutor().getLastName() + "</p>";
            emailService.sendConfirmationEmail(email, subject, content);
        }
        return convertToResponseAssignment(savedAssignment);
    }

    public List<ResponseAssignment> getAllAssignmentsByCourseId(UUID courseId) {
        Optional<Course> optionalCourse = courseRepository.findByCourseId(courseId);
        if (!optionalCourse.isPresent()) {
            throw new RuntimeException("Course not found");
        }
        Course course = optionalCourse.get();
        List<Assignment> courseList = assignmentRepository.findByCourseCourseId(course.getCourseId());
        return convertAssignmentListToResponseAssignmentList(courseList);
    }

    public List<ResponseAssignment> getDeadlinePassedAssignmentsByCourseId(UUID courseId) {
        Optional<Course> optionalCourse = courseRepository.findByCourseId(courseId);
        if (!optionalCourse.isPresent()) {
            throw new RuntimeException("Course not found");
        }
        List<Assignment> assignmentList = assignmentRepository.findByDueDatePassedAssignments();
        return convertAssignmentListToResponseAssignmentList(assignmentList);

    }

    public List<AssignmentSubmissionStats> getAssignmentsSubmittedByAllStudents(UUID courseId) {
        Optional<Course> optionalCourse = courseRepository.findByCourseId(courseId);
        if (!optionalCourse.isPresent()) {
            throw new RuntimeException("Course not found");
        }

        Course course = optionalCourse.get();
        List<AssignmentSubmissionStats> assignmentList = assignmentRepository.findAssignmentSubmittedByAllStudents(course.getCourseId());
        List<AssignmentSubmissionStats> assignmentSubmissionStatsList = new ArrayList<>();
        Long enrolled = enrollmentRepository.countByCourseCourseId(courseId);

        for (AssignmentSubmissionStats assignment : assignmentList) {
            Long completed = submissionRepository.countByAssignmentAssignmentId(assignment.getAssignmentId());
            AssignmentSubmissionStats assignmentSubmissionStats = convertToAssignmentSubmissionStats(assignment, completed, enrolled);
            assignmentSubmissionStatsList.add(assignmentSubmissionStats);
        }

        return assignmentSubmissionStatsList;
    }

    public ResponseAssignment getAssignmentByAssignmentId(UUID assignmentId) {
        Optional<Assignment> optionalAssignment = assignmentRepository.findByAssignmentId(assignmentId);
        if(!optionalAssignment.isPresent()) {
            throw new RuntimeException("Assignment not found");
        }
        Assignment assignment = optionalAssignment.get();
        return convertToResponseAssignment(assignment);
    }

    public AssignmentSubmissionStats convertToAssignmentSubmissionStats(AssignmentSubmissionStats assignment, Long completed, Long enrolled) {
        AssignmentSubmissionStats stats = new AssignmentSubmissionStats();
        stats.setAssignmentId(assignment.getAssignmentId());
        stats.setTitle(assignment.getTitle());
        stats.setCompleted(completed);
        stats.setEnrolled(enrolled);
        return stats;
    }

    public ResponseAssignment convertToResponseAssignment(Assignment assignment) {
        ResponseAssignment response = new ResponseAssignment();
        response.setTitle(assignment.getTitle());
        response.setDescription(assignment.getDescription());
        response.setDueDate(assignment.getDueDate());
        response.setMaxScore(assignment.getMaxScore());
        response.setCreatedAt(assignment.getCreatedAt());

        TutorDto tutorDto = convertToTutorDto(assignment.getTutor());
        CourseDto courseDto = convertToCourseDto(assignment.getCourse());
        response.setTutor(tutorDto);
        response.setCourse(courseDto);
        return response;
    }

    public List<ResponseAssignment> convertAssignmentListToResponseAssignmentList(List<Assignment> assignmentList) {
        List<ResponseAssignment> responseAssignments = new ArrayList<>();
        for (Assignment assignment : assignmentList) {
            ResponseAssignment responseAssignment = new ResponseAssignment();
            responseAssignment.setTitle(assignment.getTitle());
            responseAssignment.setDescription(assignment.getDescription());
            responseAssignment.setMaxScore(assignment.getMaxScore());
            responseAssignment.setDueDate(assignment.getDueDate());
            responseAssignment.setCreatedAt(assignment.getCreatedAt());
            responseAssignment.setCourse(convertToCourseDto(assignment.getCourse()));
            responseAssignment.setTutor(convertToTutorDto(assignment.getTutor()));
            responseAssignments.add(responseAssignment);
        }

        return responseAssignments;
    }

    public TutorDto convertToTutorDto(Tutor tutor) {
        TutorDto dto = new TutorDto();
        dto.setUsername(tutor.getUsername());
        dto.setFirstName(tutor.getFirstName());
        dto.setLastName(tutor.getLastName());
        dto.setEmail(tutor.getEmail());
        dto.setBirthDate(tutor.getBirthDate());
        dto.setPhoneNumber(tutor.getPhoneNumber());
        dto.setDepartment(tutor.getDepartment());
        return dto;
    }

    public CourseDto convertToCourseDto(Course course) {
        CourseDto dto = new CourseDto();
        dto.setCourseId(course.getCourseId());
        dto.setCourseTitle(course.getTitle());
        return dto;
    }
}
