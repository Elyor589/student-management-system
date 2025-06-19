package com.service.auth;

import com.dto.AuthResponse;
import com.dto.CodeData;
import com.entity.Register;
import com.entity.StudentEntity;
import com.entity.Tutor;
import com.repository.RegisterRepository;
import com.repository.StudentRepository;
import com.repository.TutorRepository;
import com.security.JwtUtil;
import com.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class AuthService {
    private final JwtUtil jwtUtil;
    private final TutorRepository tutorRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final StudentRepository studentRepository;
    private final EmailService emailService;
    private final HashMap<String, CodeData> codeMap = new HashMap<>();
    private final RegisterRepository registerRepository;

    @Autowired
    public AuthService(JwtUtil jwtUtil,
                       TutorRepository tutorRepository,
                       AuthenticationManager authenticationManager,
                       PasswordEncoder passwordEncoder,
                       StudentRepository studentRepository, EmailService emailService, RegisterRepository registerRepository) {
        this.jwtUtil = jwtUtil;
        this.tutorRepository = tutorRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.studentRepository = studentRepository;
        this.emailService = emailService;
        this.registerRepository = registerRepository;
    }

    public AuthResponse login(String username, String password) {
//        try {
//            Optional<Tutor> tutorOptional = tutorRepository.findByUsername(username);
//            if (tutorOptional.isPresent()) {
//                Tutor tutor = tutorOptional.get();
//                if (!passwordEncoder.matches(password, tutor.getPassword())){
//                    throw new RuntimeException("Incorrect password");
//                }
//
//                String role = tutor.getRole();
//                String token = jwtUtil.generateJwtToken(tutor.getUsername(), role);
//                return new AuthResponse(token);
//            }
//
//            StudentEntity student = studentRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Student not found"));
//            if (!passwordEncoder.matches(password, student.getPassword())) {
//                throw new RuntimeException("Incorrect password");
//            }
//            String role = student.getRole();
//            String token = jwtUtil.generateJwtToken(student.getUsername(), role);
//            return new AuthResponse(token);
//
//        } catch (Exception e) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login failed: " + e.getMessage());
//        }

        try {
            Optional<Register> registerOptional = registerRepository.findByUsername(username);
            if (!registerOptional.isPresent()){
                throw new RuntimeException("User not found");
            }

            Register register = registerOptional.get();
            if (!passwordEncoder.matches(password, register.getPassword())){
                throw new RuntimeException("Wrong password");
            }

            String role = register.getRole();
            String token = jwtUtil.generateJwtToken(register.getUsername(), role);
            return new AuthResponse(token);
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login failed" + e.getMessage());
        }
    }

    public String forgotPassword(String phoneNumber) {
//        Optional<StudentEntity> optionalStudent = studentRepository.findByPhoneNumber(phoneNumber);
//
        String code = String.valueOf(ThreadLocalRandom.current().nextLong(100000, 1000000));
        LocalDateTime codeExpiry = LocalDateTime.now().plusMinutes(10);
        String subject = "Change Password Confirmation Code";
        String content = "<p>Verification code for password change: <b>" + code + "<b/></p>";
//
//        if (optionalStudent.isPresent()) {
//            StudentEntity student = optionalStudent.get();
//            codeMap.put(code, new CodeData(phoneNumber, codeExpiry));
//            emailService.sendConfirmationEmail(student.getEmail(), subject, content);
//            return "Code sent to " + student.getEmail();
//        }
//
//        Tutor tutor = tutorRepository.findByPhoneNumber(phoneNumber).orElseThrow(() -> new RuntimeException("Tutor not found"));

        Register register = registerRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        codeMap.put(code, new CodeData(phoneNumber, codeExpiry));
        emailService.sendConfirmationEmail(register.getEmail(), subject, content);

        return "Code sent to " + register.getEmail();
    }

    public String changePassword(String code, String newPassword) {
        CodeData codeData = codeMap.get(code);
        if (codeData == null) {
            throw new RuntimeException("This code is used");
        }

        if (codeData.getCodeExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Code is expired");
        }
        String phoneNumber = codeData.getPhoneNumber();
        Optional<StudentEntity> optionalStudent = studentRepository.findByPhoneNumber(phoneNumber);
        if (optionalStudent.isPresent()) {
            StudentEntity student = optionalStudent.get();
            student.setPassword(passwordEncoder.encode(newPassword));
            studentRepository.save(student);
            codeMap.remove(code);
            return "Password changed";
        }

        Optional<Tutor> optionalTutor = tutorRepository.findByPhoneNumber(phoneNumber);
        if (optionalTutor.isPresent()) {
            Tutor tutor = optionalTutor.get();
            tutor.setPassword(passwordEncoder.encode(newPassword));
            tutorRepository.save(tutor);
            codeMap.remove(code);
            return "Password changed";
        }

        throw new RuntimeException("User not found");
    }
}
