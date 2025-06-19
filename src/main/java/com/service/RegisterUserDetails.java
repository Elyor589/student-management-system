package com.service;

import com.entity.Register;
import com.repository.RegisterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class RegisterUserDetails implements UserDetailsService {
    @Autowired
    private RegisterRepository registerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Register register = registerRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new User(
                register.getUsername(),
                register.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(("ROLE_" + register.getRole())))
        );
    }
}
