package com.movieapp.movieapplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movieapp.movieapplication.JwtUtil;
import com.movieapp.movieapplication.model.User;
import com.movieapp.movieapplication.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtUtil jwtUtil;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private UserController userController;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Bez close()
        userController = new UserController(userService, jwtUtil);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper(); // Ręczna inicjalizacja
    }


    @Test
    void testRegisterUser_Success() throws Exception {
        // Utworzenie obiektu RegisterRequest
        var registerRequest = new RegisterRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password");

        // Mockowanie metody registerUser, aby zwracała nowego użytkownika
        when(userService.registerUser(registerRequest.getUsername(), registerRequest.getEmail(), registerRequest.getPassword()))
                .thenReturn(new User(registerRequest.getUsername(), registerRequest.getEmail(), "passwordHash", "ROLE_USER"));

        // Wysłanie żądania POST do endpointu /register
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully"));

        // Weryfikacja, czy metoda registerUser została wywołana raz
        verify(userService, times(1))
                .registerUser(registerRequest.getUsername(), registerRequest.getEmail(), registerRequest.getPassword());
    }


    @Test
    void testRegisterUser_EmailAlreadyInUse() throws Exception {
        var registerRequest = new RegisterRequest("testuser", "test@example.com", "password");

        doThrow(new IllegalArgumentException("Email is already in use."))
                .when(userService).registerUser(registerRequest.getUsername(), registerRequest.getEmail(), registerRequest.getPassword());

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Email is already in use."));

        verify(userService, times(1)).registerUser(registerRequest.getUsername(), registerRequest.getEmail(), registerRequest.getPassword());
    }

    @Test
    void testLoginUser_Success() throws Exception {
        var loginRequest = new LoginRequest("test@example.com", "password");
        User mockUser = new User("testuser", "test@example.com", "passwordHash", "ROLE_USER");
        String token = "mockToken";

        when(userService.loginUser(loginRequest.getEmail(), loginRequest.getPassword())).thenReturn(mockUser);
        when(jwtUtil.generateToken(mockUser.getId(), mockUser.getUsername(), mockUser.getRole(), mockUser.getEmail())).thenReturn(token);

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Token: " + token));

        verify(userService, times(1)).loginUser(loginRequest.getEmail(), loginRequest.getPassword());
        verify(jwtUtil, times(1)).generateToken(mockUser.getId(), mockUser.getUsername(), mockUser.getRole(), mockUser.getEmail());
    }

    @Test
    void testLoginUser_InvalidCredentials() throws Exception {
        var loginRequest = new LoginRequest("test@example.com", "wrongpassword");

        when(userService.loginUser(loginRequest.getEmail(), loginRequest.getPassword()))
                .thenThrow(new IllegalArgumentException("Invalid email or password."));

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid email or password."));

        verify(userService, times(1)).loginUser(loginRequest.getEmail(), loginRequest.getPassword());
    }

    @Test
    void testGetAllUsers() throws Exception {
        User user1 = new User("testuser1", "test1@example.com", "passwordHash", "ROLE_USER");
        User user2 = new User("testuser2", "test2@example.com", "passwordHash", "ROLE_ADMIN");

        when(userService.getAllUsers()).thenReturn(List.of(user1, user2));

        mockMvc.perform(get("/api/users/get-all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("testuser1"))
                .andExpect(jsonPath("$[1].username").value("testuser2"));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void testGetUserById_Found() throws Exception {
        String userId = "1";
        User user = new User("testuser", "test@example.com", "passwordHash", "ROLE_USER");

        when(userService.getUserById(userId)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));

        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void testGetUserById_NotFound() throws Exception {
        String userId = "1";

        when(userService.getUserById(userId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/" + userId))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void testUpdateUser_Success() throws Exception {
        String userId = "1";
        User updatedUser = new User("newUsername", "new@example.com", "newPasswordHash", "ROLE_ADMIN");

        when(userService.updateUser(eq(userId), any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/api/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("newUsername"));

        verify(userService, times(1)).updateUser(eq(userId), any(User.class));
    }

    @Test
    void testDeleteUser_Success() throws Exception {
        String userId = "1";

        doNothing().when(userService).deleteUser(userId);

        mockMvc.perform(delete("/api/users/" + userId))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(userId);
    }

    @Test
    void testDeleteUser_NotFound() throws Exception {
        String userId = "1";

        doThrow(new IllegalArgumentException("User not found with id: " + userId))
                .when(userService).deleteUser(userId);

        mockMvc.perform(delete("/api/users/" + userId))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).deleteUser(userId);
    }
}