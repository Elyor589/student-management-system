package com.security;

import com.service.RegisterUserDetails;
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
    private final RegisterUserDetails registerUserDetails;

    public JwtAuthFilter(JwtUtil jwtUtil,
                         RegisterUserDetails registerUserDetails) {
        this.jwtUtil = jwtUtil;
        this.registerUserDetails = registerUserDetails;
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

            userDetails = registerUserDetails.loadUserByUsername(username);
            filterChain.doFilter(request, response);

            if (jwtUtil.validateJwtToken(token, userDetails)) {
                setAuthenticationContext(token, request,userDetails);
                filterChain.doFilter(request, response);
            }
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthenticationContext(String token, HttpServletRequest request,UserDetails userDetails) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


}
