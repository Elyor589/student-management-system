package com.service;

import com.dto.DtoMapper;
import com.dto.EnrollmentDto;
import com.entity.Course;
import com.entity.Enrollment;
import com.entity.StudentEntity;
import com.repository.CourseRepository;
import com.repository.EnrollmentRepository;
import com.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final EmailService emailService;

    public EnrollmentService(EnrollmentRepository enrollmentRepository,
                             CourseRepository courseRepository,
                             StudentRepository studentRepository,
                             EmailService emailService) {
        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
        this.emailService = emailService;
    }

    public EnrollmentDto enroll(String studentId, UUID courseId) {
        StudentEntity studentEntity = studentRepository.findByStudentId(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
        Course course = courseRepository.findByCourseId(courseId).orElseThrow(() -> new RuntimeException("Course not found"));

        boolean alreadyEnrolled = enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId);
        if (alreadyEnrolled) {
            throw new RuntimeException("Enrollment already exists");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setEnrollmentId(UUID.randomUUID());
        enrollment.setStudent(studentEntity);
        enrollment.setCourse(course);
        enrollment.setEnrollmentDate(LocalDate.now());
        enrollment.setStatus("ACTIVE");

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        String courseTitle = course.getTitle();
        String studentName = studentEntity.getFirstName();
        String email = studentEntity.getEmail();
        String subject = "Enrollment Confirmation";
        String enrolledCourses = studentEntity.getEnrollments().stream()
                .map(e -> e.getCourse().getTitle())
                .collect(Collectors.joining(", "));

        String content = "<p>Dear " + studentName + "</p>"
                + "<p>StudentId: <b>" + studentEntity.getStudentId() + "</b></p>"
                + "<p>Enrolled courses: <b>" + enrolledCourses + "</b></p>"
                + "<p>Just now you have been enrolled in the course <b>" + courseTitle + "</b></p>"
                + "<p>Best regards</p>";

        emailService.sendConfirmationEmail(email, subject, content);
        return DtoMapper.toEnrollmentDto(savedEnrollment);
    }

    public List<EnrollmentDto> getAllEnrollments() {
        List<Enrollment> enrollmentsList = enrollmentRepository.findAll();
        return enrollmentsList.stream()
                .map(DtoMapper::toEnrollmentDto)
                .toList();
    }

    public List<EnrollmentDto> getEnrollmentByStudentId(String studentId) {
        studentRepository.findByStudentId(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
        return enrollmentRepository.findByStudentId(studentId).stream()
                .map(DtoMapper::toEnrollmentDto)
                .toList();
    }

    public List<EnrollmentDto> getEnrollmentByCourseId(UUID courseId) {
        courseRepository.findByCourseId(courseId).orElseThrow(() -> new RuntimeException("Course not found"));
        return enrollmentRepository.findByCourseId(courseId).stream()
                .map(enrollment -> {
                    EnrollmentDto dto = DtoMapper.toEnrollmentDto(enrollment);
                    dto.setStudent(DtoMapper.studentDto(enrollment.getStudent()));
                    return dto;
                }).toList();
    }
}
