package com.service;

import com.entity.Tutor;
import com.repository.TutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service("tutorUserDetailsService")
public class TutorUserDetailsService implements UserDetailsService {
    @Autowired
    private TutorRepository tutorRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Tutor tutor = tutorRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
                tutor.getUsername(),
                tutor.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + tutor.getRole()))
        );
    }
}
