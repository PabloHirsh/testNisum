package com.testNisum.service;

import com.testNisum.exception.EmailAlreadyExistsException;
import com.testNisum.model.User;
import com.testNisum.model.UserResponseDto;
import com.testNisum.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser_Success() throws EmailAlreadyExistsException {
        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDto createdUser = userService.createUser(user);

        assertNotNull(createdUser);

        assertEquals(true, createdUser.getIsActive() );
    }

    @Test
    void testGetUserById_Success() {
        UUID userId    = UUID.randomUUID();
        UUID usuarioId = null;
        User user      = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.getUserById(userId);

        if (foundUser.isPresent()) {
            User usuario = foundUser.get();
             usuarioId = usuario.getId();
            System.out.println("User ID: " + userId);
        } else {
            System.out.println("User not found");
        }

        assertNotNull(foundUser);
        assertEquals(userId, usuarioId );
    }

    @Test
    void testGetUserById_NotFound() {
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Optional<User> foundUser = userService.getUserById(userId);

        assertNull(foundUser);
    }
}
