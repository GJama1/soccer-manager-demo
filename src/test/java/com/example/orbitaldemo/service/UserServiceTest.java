package com.example.orbitaldemo.service;

import com.example.orbitaldemo.model.domain.database.UserEntity;
import com.example.orbitaldemo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_ShouldCreateUser_WhenEmailNotInUse() {
        String email = "test@example.com";
        String rawPassword = "password123";
        String encodedPassword = "encodedPassword123";

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserEntity result = userService.createUser(email, rawPassword);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
        assertEquals(encodedPassword, result.getPassword());
        verify(userRepository).existsByEmail(email);
        verify(passwordEncoder).encode(rawPassword);
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    void createUser_ShouldThrowException_WhenEmailAlreadyInUse() {
        String email = "test@example.com";
        String rawPassword = "password123";

        when(userRepository.existsByEmail(email)).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.createUser(email, rawPassword));
        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals("Email already in use", exception.getReason());
        verify(userRepository).existsByEmail(email);
        verifyNoInteractions(passwordEncoder);
        verify(userRepository, never()).save(any(UserEntity.class));
    }

}