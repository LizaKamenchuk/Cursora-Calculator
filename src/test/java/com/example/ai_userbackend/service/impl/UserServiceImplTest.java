package com.example.ai_userbackend.service.impl;

import com.example.ai_userbackend.dto.UserDto;
import com.example.ai_userbackend.entity.User;
import com.example.ai_userbackend.mapper.UserMapper;
import com.example.ai_userbackend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserDto userDto;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");

        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("Test User");
        userDto.setEmail("test@example.com");

        pageable = PageRequest.of(0, 10);
    }

    @Test
    void createUser_ShouldReturnCreatedUser() {
        when(userMapper.toEntity(any(UserDto.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDto(any(User.class))).thenReturn(userDto);

        UserDto result = userService.createUser(userDto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(userDto.getId());
        assertThat(result.getName()).isEqualTo(userDto.getName());
        assertThat(result.getEmail()).isEqualTo(userDto.getEmail());

        verify(userMapper).toEntity(userDto);
        verify(userRepository).save(user);
        verify(userMapper).toDto(user);
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userDto);

        UserDto result = userService.getUserById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(userDto.getId());
        assertThat(result.getName()).isEqualTo(userDto.getName());
        assertThat(result.getEmail()).isEqualTo(userDto.getEmail());

        verify(userRepository).findById(1L);
        verify(userMapper).toDto(user);
    }

    @Test
    void getUserById_WhenUserDoesNotExist_ShouldThrowException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("User not found with id: 1");

        verify(userRepository).findById(1L);
        verify(userMapper, never()).toDto(any());
    }

    @Test
    void getAllUsers_ShouldReturnPageOfUsers() {
        List<User> users = List.of(user);
        Page<User> userPage = new PageImpl<>(users);
        when(userRepository.findAll(pageable)).thenReturn(userPage);
        when(userMapper.toDto(user)).thenReturn(userDto);

        Page<UserDto> result = userService.getAllUsers(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0)).isEqualTo(userDto);

        verify(userRepository).findAll(pageable);
        verify(userMapper).toDto(user);
    }

    @Test
    void updateUser_WhenUserExists_ShouldReturnUpdatedUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toEntity(userDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        UserDto result = userService.updateUser(1L, userDto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(userDto.getId());
        assertThat(result.getName()).isEqualTo(userDto.getName());
        assertThat(result.getEmail()).isEqualTo(userDto.getEmail());

        verify(userRepository).findById(1L);
        verify(userMapper).toEntity(userDto);
        verify(userRepository).save(user);
        verify(userMapper).toDto(user);
    }

    @Test
    void updateUser_WhenUserDoesNotExist_ShouldThrowException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateUser(1L, userDto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("User not found with id: 1");

        verify(userRepository).findById(1L);
        verify(userMapper, never()).toEntity(any(UserDto.class));
        verify(userRepository, never()).save(any());
    }

    @Test
    void deleteUser_WhenUserExists_ShouldDeleteUser() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository).existsById(1L);
        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUser_WhenUserDoesNotExist_ShouldThrowException() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> userService.deleteUser(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("User not found with id: 1");

        verify(userRepository).existsById(1L);
        verify(userRepository, never()).deleteById(any());
    }
} 