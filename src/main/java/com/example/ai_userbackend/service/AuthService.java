package com.example.ai_userbackend.service;

import com.example.ai_userbackend.dto.LoginRequest;
import com.example.ai_userbackend.dto.LoginResponse;
import com.example.ai_userbackend.dto.RegisterRequest;

public interface AuthService {
    void register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
} 