package com.service;

import com.entity.StudentEntity;
import com.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service("studentUserDetailsService")
public class StudentUserDetailsService implements UserDetailsService {
    @Autowired
    private StudentRepository studentRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        StudentEntity student = studentRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Student not found"));

        return new org.springframework.security.core.userdetails.User(
                student.getUsername(),
                student.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_" + student.getRole()))
        );
    }
}
