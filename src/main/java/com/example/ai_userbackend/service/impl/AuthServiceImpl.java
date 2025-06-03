package com.example.ai_userbackend.service.impl;

import com.example.ai_userbackend.dto.LoginRequest;
import com.example.ai_userbackend.dto.LoginResponse;
import com.example.ai_userbackend.dto.RegisterRequest;
import com.example.ai_userbackend.dto.UserDto;
import com.example.ai_userbackend.entity.Auth;
import com.example.ai_userbackend.entity.User;
import com.example.ai_userbackend.exception.AuthenticationException;
import com.example.ai_userbackend.mapper.UserMapper;
import com.example.ai_userbackend.repository.AuthRepository;
import com.example.ai_userbackend.repository.UserRepository;
import com.example.ai_userbackend.security.JwtTokenProvider;
import com.example.ai_userbackend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public void register(RegisterRequest request) {
        if (authRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = userMapper.toEntity(request);
        user = userRepository.save(user);

        Auth auth = new Auth();
        auth.setEmail(request.getEmail());
        auth.setPassword(passwordEncoder.encode(request.getPassword()));
        auth.setUser(user);
        authRepository.save(auth);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            if (!authentication.isAuthenticated()) {
                throw new BadCredentialsException("Authentication failed");
            }

            String token = tokenProvider.generateToken(authentication);
            return new LoginResponse(token);
        } catch (BadCredentialsException e) {
            throw new IllegalArgumentException("Invalid email or password");
        } catch (UsernameNotFoundException e) {
            throw new IllegalArgumentException("User not found");
        } catch (Exception e) {
            throw new IllegalArgumentException("Authentication failed: " + e.getMessage());
        }
    }
} 