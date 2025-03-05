package com.backend.UniErrands.service;

import com.backend.UniErrands.model.User;
import com.backend.UniErrands.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;


public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateUser_Success() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setEmail("testuser@vitapstudent.ac.in");
        user.setRole("REQUESTER");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertEquals("testuser", createdUser.getUsername());
    }


    @Test
    public void testCreateUser_EmailAlreadyExists() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setEmail("testuser@vitapstudent.ac.in");
        user.setRole("REQUESTER");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(user);
        });

        assertEquals("User with the same email already exists.", exception.getMessage());
    }

    @Test
    public void testGetUserById_Success() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User foundUser = userService.getUserById(1L);

        assertNotNull(foundUser);
        assertEquals("testuser", foundUser.getUsername());
    }

    @Test
    public void testDeleteUser_Success() {
        Long userId = 1L;

        when(userRepository.existsById(userId)).thenReturn(true);

        boolean isDeleted = userService.deleteUserById(userId);

        assertTrue(isDeleted);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    public void testDeleteUser_NotFound() {
        Long userId = 1L;

        when(userRepository.existsById(userId)).thenReturn(false);

        boolean isDeleted = userService.deleteUserById(userId);

        assertFalse(isDeleted);
        verify(userRepository, never()).deleteById(userId);
    }

}
