package com.example.ai_userbackend.service.impl;

import com.example.ai_userbackend.dto.LoginRequest;
import com.example.ai_userbackend.dto.LoginResponse;
import com.example.ai_userbackend.dto.RegisterRequest;
import com.example.ai_userbackend.entity.Auth;
import com.example.ai_userbackend.entity.User;
import com.example.ai_userbackend.mapper.UserMapper;
import com.example.ai_userbackend.repository.AuthRepository;
import com.example.ai_userbackend.repository.UserRepository;
import com.example.ai_userbackend.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AuthRepository authRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private User user;
    private Auth auth;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password");
        registerRequest.setName("Test User");

        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");

        auth = new Auth();
        auth.setId(1L);
        auth.setEmail("test@example.com");
        auth.setPassword("encodedPassword");
        auth.setUser(user);

        authentication = new UsernamePasswordAuthenticationToken(
                auth.getEmail(), loginRequest.getPassword());
    }

    @Test
    void register_ShouldCreateNewUserAndAuth() {
        when(userMapper.toEntity(any(RegisterRequest.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(authRepository.save(any(Auth.class))).thenReturn(auth);

        authService.register(registerRequest);

        verify(userMapper).toEntity(any(RegisterRequest.class));
        verify(userRepository).save(user);
        verify(passwordEncoder).encode(registerRequest.getPassword());
        verify(authRepository).save(any(Auth.class));
    }

    @Test
    void register_WhenEmailExists_ShouldThrowException() {
        when(authRepository.findByEmail(registerRequest.getEmail()))
                .thenReturn(Optional.of(auth));

        assertThatThrownBy(() -> authService.register(registerRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email already exists");

        verify(authRepository).findByEmail(registerRequest.getEmail());
        verify(userRepository, never()).save(any());
        verify(authRepository, never()).save(any());
    }

    @Test
    void login_ShouldReturnToken() {
        when(authRepository.findByEmail(loginRequest.getEmail()))
                .thenReturn(Optional.of(auth));
        when(authenticationManager.authenticate(any()))
                .thenReturn(authentication);
        when(tokenProvider.generateToken(any(Authentication.class)))
                .thenReturn("jwtToken");

        LoginResponse response = authService.login(loginRequest);

        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("jwtToken");

        verify(authRepository).findByEmail(loginRequest.getEmail());
        verify(authenticationManager).authenticate(any());
        verify(tokenProvider).generateToken(authentication);
    }

    @Test
    void login_WhenUserNotFound_ShouldThrowException() {
        when(authRepository.findByEmail(loginRequest.getEmail()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid email or password");

        verify(authRepository).findByEmail(loginRequest.getEmail());
        verify(authenticationManager, never()).authenticate(any());
        verify(tokenProvider, never()).generateToken(any());
    }

    @Test
    void login_WhenAuthenticationFails_ShouldThrowException() {
        when(authRepository.findByEmail(loginRequest.getEmail()))
                .thenReturn(Optional.of(auth));
        when(authenticationManager.authenticate(any()))
                .thenThrow(new RuntimeException("Authentication failed"));

        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid email or password");

        verify(authRepository).findByEmail(loginRequest.getEmail());
        verify(authenticationManager).authenticate(any());
        verify(tokenProvider, never()).generateToken(any());
    }
} 