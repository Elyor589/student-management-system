package com.controller;

import com.dto.AuthResponse;
import com.dto.IsAvailable;
import com.dto.RegisterDto;
import com.dto.RegisterRequest;
import com.service.RegisterService;
import com.service.auth.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final RegisterService registerService;

    public AuthController(AuthService authService,
                          RegisterService registerService) {
        this.authService = authService;
        this.registerService = registerService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            RegisterDto register = registerService.register(request);
            return ResponseEntity.ok(register);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestParam String username, @RequestParam String password) {
        return ResponseEntity.ok(authService.login(username, password));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String phoneNumber) {
        try {
            String forgotPassword = authService.forgotPassword(phoneNumber);
            return ResponseEntity.ok(forgotPassword);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestParam String code, @RequestParam String newPassword) {
        try {
            String changePassword = authService.changePassword(code, newPassword);
            return ResponseEntity.ok(changePassword);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/checkUsername")
    public ResponseEntity<?> checkUsername(@RequestParam String username) {
        IsAvailable availableUsername = registerService.isAvailableUsername(username);
        return ResponseEntity.ok(availableUsername);
    }

}
