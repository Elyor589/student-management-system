package com.service;

import com.dto.IsAvailable;
import com.dto.RegisterDto;
import com.dto.RegisterRequest;
import com.entity.Register;
import com.repository.RegisterRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class RegisterService {
    private final RegisterRepository registerRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterService(RegisterRepository registerRepository,
                           PasswordEncoder passwordEncoder) {
        this.registerRepository = registerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public RegisterDto register(RegisterRequest request){
        Optional<Register> registerOptional = registerRepository.findByUsername(request.getUsername());
        if (registerOptional.isPresent()){
            throw new RuntimeException("Username already in use");
        }

        Optional<Register> emailAlreadyInUse = registerRepository.findByEmail(request.getEmail());
        if (emailAlreadyInUse.isPresent()){
            throw new RuntimeException("Email already in use");
        }

        Register register = new Register();
        register.setRegisterId(UUID.randomUUID());
        register.setUsername(request.getUsername());
        register.setPassword(passwordEncoder.encode(request.getPassword()));
        register.setEmail(request.getEmail());
        register.setPhoneNumber(request.getPhoneNumber());
        register.setRole("USER");

        return convertToRegisterDto(registerRepository.save(register));
    }

    public IsAvailable isAvailableUsername(String username){
        boolean isAvailable = registerRepository.findByUsername(username).isEmpty();
        return new IsAvailable(isAvailable);

    }

    private RegisterDto convertToRegisterDto(Register register){
        RegisterDto registerDto = new RegisterDto();
        registerDto.setRegisterId(register.getRegisterId());
        registerDto.setUsername(register.getUsername());
        registerDto.setPassword(register.getPassword());
        registerDto.setPhoneNumber(register.getPhoneNumber());
        registerDto.setEmail(register.getEmail());
        return registerDto;
    }
}
