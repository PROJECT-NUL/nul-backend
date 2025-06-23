package com.springboot.service;

import com.springboot.exceptions.RegistrationException;
import com.springboot.repository.UserRepository;
import com.springboot.security.dto.RegistrationRequest;
import com.springboot.utils.ExceptionMessageAccessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserValidationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ExceptionMessageAccessor exceptionMessageAccessor;

    @InjectMocks
    private UserValidationService userValidationService;

    @Test
    void validateUser_whenEmailExists_shouldThrowRegistrationException() {
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail("test@example.com");
        request.setUsername("testuser");

        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);
        when(exceptionMessageAccessor.getMessage(null, "email_already_exists"))
                .thenReturn("exists");

        RegistrationException exception = assertThrows(RegistrationException.class,
                () -> userValidationService.validateUser(request));

        assertEquals("exists", exception.getErrorMessage());
        verify(userRepository).existsByEmail("test@example.com");
    }

    @Test
    void validateUser_whenUsernameExists_shouldThrowRegistrationException() {
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail("test@example.com");
        request.setUsername("testuser");

        when(userRepository.existsByUsername("testuser")).thenReturn(true);
        when(exceptionMessageAccessor.getMessage(null, "username_already_exists"))
                .thenReturn("exists");

        RegistrationException exception = assertThrows(RegistrationException.class,
                () -> userValidationService.validateUser(request));

        assertEquals("exists", exception.getErrorMessage());
        verify(userRepository).existsByUsername("testuser");
    }
}
