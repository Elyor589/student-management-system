package com.service;

import com.dto.CourseDto;
import com.dto.TutorDto;
import com.dto.assignment.RequestAssignment;
import com.dto.assignment.ResponseAssignment;
import com.entity.Assignment;
import com.entity.Course;
import com.entity.Tutor;
import com.repository.AssignmentRepository;
import com.repository.CourseRepository;
import com.repository.TutorRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AssignmentService {

    private final TutorRepository tutorRepository;
    private final AssignmentRepository assignmentRepository;
    private final CourseRepository courseRepository;

    public AssignmentService(TutorRepository tutorRepository,
                             AssignmentRepository assignmentRepository,
                             CourseRepository courseRepository) {
        this.tutorRepository = tutorRepository;
        this.assignmentRepository = assignmentRepository;
        this.courseRepository = courseRepository;
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
        return convertToResponseAssignment(savedAssignment);
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
