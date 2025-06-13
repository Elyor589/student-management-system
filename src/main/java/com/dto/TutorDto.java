package com.dto;

import com.entity.Assignment;
import com.entity.TutorCourse;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class TutorDto {
    private String tutorId;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private LocalDate birthDate;
    private String email;
    private String phoneNumber;
    private String department;
//    private List<TutorCourse> tutorCourses;
//    private List<Assignment> assignments;
}
