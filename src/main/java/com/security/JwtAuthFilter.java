package com.security;

import com.service.StudentUserDetailsService;
import com.service.TutorUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final TutorUserDetailsService tutorUserDetailsService;
    private final StudentUserDetailsService studentUserDetailsService;

    public JwtAuthFilter(JwtUtil jwtUtil,
                         TutorUserDetailsService tutorUserDetailsService,
                         StudentUserDetailsService studentUserDetailsService) {
        this.jwtUtil = jwtUtil;
        this.tutorUserDetailsService = tutorUserDetailsService;
        this.studentUserDetailsService = studentUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = jwtUtil.extractUsername(token);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            String role = jwtUtil.extractRole(token);
            UserDetails userDetails = null;

            if ("student".equals(role)) {
                userDetails = studentUserDetailsService.loadUserByUsername(username);
            } else if ("tutor".equals(role)) {
                userDetails = tutorUserDetailsService.loadUserByUsername(username);
            } else {
                filterChain.doFilter(request, response);
                return;
            }

            if (jwtUtil.validateJwtToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }

}
