package com.example.ai_userbackend.service;

import com.example.ai_userbackend.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserDto createUser(UserDto userDto);
    UserDto getUserById(Long id);
    Page<UserDto> getAllUsers(Pageable pageable);
    UserDto updateUser(Long id, UserDto userDto);
    void deleteUser(Long id);
} 