package com.service;

import com.dto.TutorDto;
import com.dto.stats.AssignmentStatsResponse;
import com.entity.Course;
import com.entity.Tutor;
import com.repository.CourseRepository;
import com.repository.TutorRepository;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class TutorService {
    private final TutorRepository tutorRepository;
    private final StudentService studentService;
    private final PasswordEncoder passwordEncoder;
    private final CourseRepository courseRepository;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public TutorDto createTutor(TutorDto tutorDto) {
        Tutor tutor = new Tutor();

        Optional<Tutor> tutorOptional = tutorRepository.findByTutorId(tutor.getTutorId());
        if (tutorOptional.isPresent()) {
            throw new RuntimeException("Tutor already exists");
        }

        boolean validPhoneNumber = studentService.phoneNumberCheck(tutorDto.getPhoneNumber());
        if (!validPhoneNumber) {
            throw new RuntimeException("Invalid phone number");
        }

        tutor.setFirstName(tutorDto.getFirstName());
        tutor.setLastName(tutorDto.getLastName());
        tutor.setEmail(tutorDto.getEmail());
        tutor.setPhoneNumber(tutorDto.getPhoneNumber());
        tutor.setBirthDate(tutorDto.getBirthDate());
        tutor.setIsActive(true);
        tutor.setUsername(tutorDto.getUsername());
        tutor.setPassword(passwordEncoder.encode(tutorDto.getPassword()));
        tutor.setCreatedAt(LocalDateTime.now());
        tutor.setDepartment(tutorDto.getDepartment());
        tutor.setRole("tutor");
        tutorRepository.save(tutor);
        return tutorDto;
    }

    public TutorDto getTutorByTutorId(String tutorId) {
        Optional<Tutor> tutorOptional = tutorRepository.findByTutorId(tutorId);
        if (!tutorOptional.isPresent()) {
            throw new RuntimeException("Tutor not found");
        }
        Tutor tutor = tutorOptional.get();
        TutorDto tutorDto = new TutorDto();
        tutorDto.setFirstName(tutor.getFirstName());
        tutorDto.setLastName(tutor.getLastName());
        tutorDto.setUsername(tutor.getUsername());
        tutorDto.setEmail(tutor.getEmail());
        tutorDto.setPhoneNumber(tutor.getPhoneNumber());
        tutorDto.setBirthDate(tutor.getBirthDate());
        tutorDto.setDepartment(tutor.getDepartment());
        return tutorDto;
    }

    public AssignmentStatsResponse getAssignmentStatsByCourseId(UUID courseId) {

        Optional<Course> optionalCourse = courseRepository.findByCourseId(courseId);
        if (!optionalCourse.isPresent()) {
            throw new RuntimeException("Course not found");
        }

        String totalAssignmentsSql = "select count(a) as total_assignment from student_management.assignments a where course_id = :courseId";

        String totalEnrollmentsSql = "select count(e.enrollment_id) as total_enrollments from student_management.enrollment e\n" +
                "join student_management.students s on e.student_id = s.student_id\n" +
                "where e.course_id = :courseId";

        String totalSubmissionsSql = "select count(s.submission_id) as total_submissions " +
                "from student_management.submissions s " +
                "join student_management.assignments a on s.assignment_id = a.assignment_id " +
                "WHERE a.course_id = :courseId";

        String totalNoSubmissionsSql = "SELECT COUNT(*) AS total_no_submissions\n" +
                "FROM student_management.enrollment e\n" +
                "WHERE e.course_id = :courseId\n" +
                "  AND (\n" +
                "          SELECT COUNT(*)\n" +
                "          FROM student_management.submissions s\n" +
                "                   JOIN student_management.assignments a ON s.assignment_id = a.assignment_id\n" +
                "          WHERE s.student_id = e.student_id AND a.course_id = :courseId\n" +
                "      ) < (\n" +
                "          SELECT COUNT(*)\n" +
                "          FROM student_management.assignments a\n" +
                "          WHERE a.course_id = :courseId\n" +
                "      )";

        Map<String, Object> params = new HashMap<>();
        params.put("courseId", courseId);

        Long totalAssignments = namedParameterJdbcTemplate.queryForObject(totalAssignmentsSql, params, Long.class);
        Long totalEnrollments = namedParameterJdbcTemplate.queryForObject(totalEnrollmentsSql, params, Long.class);
        Long totalSubmissions = namedParameterJdbcTemplate.queryForObject(totalSubmissionsSql, params, Long.class);
        Long totalNoSubmissions = namedParameterJdbcTemplate.queryForObject(totalNoSubmissionsSql, params, Long.class);

        return new AssignmentStatsResponse(
                totalAssignments,
                totalEnrollments,
                totalSubmissions,
                totalNoSubmissions
        );
    }
}
