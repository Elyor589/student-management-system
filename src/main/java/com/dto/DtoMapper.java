package com.dto;


import com.entity.Course;
import com.entity.Enrollment;
import com.entity.StudentEntity;

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

        dto.setEnrollments(enrollmentDto);
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

}
