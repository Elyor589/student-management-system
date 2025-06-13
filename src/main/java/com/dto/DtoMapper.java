package com.dto;


import com.dto.assignment.AssignmentDto;
import com.dto.submission.ResponseSubmitAssignment;
import com.entity.*;

import java.util.List;

public class DtoMapper {
    public static StudentDto studentDto(StudentEntity student){
        StudentDto dto = new StudentDto();
        dto.setStudentId(student.getStudentId());
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());
        dto.setEmail(student.getEmail());
        dto.setDateOfBirth(student.getDateOfBirth());
        dto.setAge(student.getAge());
        dto.setGender(student.getGender());
        dto.setPhoneNumber(student.getPhoneNumber());

        List<EnrollmentDto> enrollmentDto = student.getEnrollments().stream()
                .map(DtoMapper::toEnrollmentDto)
                .toList();

//        dto.setEnrollments(enrollmentDto);
        return dto;
    }


    public static EnrollmentDto toEnrollmentDto(Enrollment enrollment){
        EnrollmentDto dto = new EnrollmentDto();
        dto.setEnrollmentId(enrollment.getEnrollmentId());
        dto.setCourseTitle(enrollment.getCourse().getTitle());
        dto.setEnrollmentDate(enrollment.getEnrollmentDate());
        dto.setGrade(enrollment.getGrade());
        dto.setStatus(enrollment.getStatus());
        return dto;
    }

    public static CourseDto courseDto(Course course){
        CourseDto dto = new CourseDto();
        dto.setCourseId(course.getCourseId());
        dto.setCourseTitle(course.getTitle());
        List<EnrollmentDto> enrollmentDto = course.getEnrollments().stream()
                .map(DtoMapper::toEnrollmentDto)
                .toList();

        dto.setEnrollments(enrollmentDto);
        return dto;
    }

    public static ResponseSubmitAssignment mapToSubmissionResponse(Submission submission) {
        ResponseSubmitAssignment response = new ResponseSubmitAssignment();
        response.setAssignmentId(submission.getAssignment().getAssignmentId());
        response.setStudentId(submission.getStudent().getStudentId());
        response.setSubmissionId(submission.getSubmissionId());
        response.setStudentName(submission.getStudent().getFirstName());
        response.setSubmitDate(submission.getSubmissionDate());
        response.setFileName(submission.getFilePath());
        response.setComment(submission.getComments());
        response.setGraded(submission.isGraded());

        AssignmentDto assignmentDto = convertAssignmentToAssignmentDto(submission.getAssignment());
        response.setAssignment(assignmentDto);
        return response;
    }

    public static AssignmentDto convertAssignmentToAssignmentDto(Assignment assignment) {
        AssignmentDto dto = new AssignmentDto();
        dto.setAssignmentId(assignment.getAssignmentId());
        dto.setTitle(assignment.getTitle());
        dto.setDescription(assignment.getDescription());
        dto.setMaxScore(assignment.getMaxScore());
        dto.setDueDate(assignment.getDueDate());
//        dto.setCourse(DtoMapper.courseDto(assignment.getCourse()));
        return dto;
    }

}
