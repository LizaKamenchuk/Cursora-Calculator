package com.example.ai_userbackend.service;

import com.example.ai_userbackend.entity.Auth;

public interface JwtService {
    String generateToken(Auth auth);
    String extractUsername(String token);
    boolean isTokenValid(String token);
} 