package com.movieapp.movieapplication.controller;

import com.movieapp.movieapplication.model.User;
import com.movieapp.movieapplication.service.UserService;
import com.movieapp.movieapplication.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    // Endpoint do rejestracji użytkownika
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        try {
            userService.registerUser(request.getUsername(), request.getEmail(), request.getPassword());
            return ResponseEntity.ok("User registered successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint do logowania użytkownika
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        try {
            User user = userService.loginUser(request.getEmail(), request.getPassword());
            String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole(), user.getEmail()); // Generowanie tokenu JWT
//            String validateToken = jwtUtil.validateToken(token);
            System.out.println(jwtUtil.extractRole(token));
            return ResponseEntity.ok("Token: " + token); // Zwrócenie tokenu
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get-all")
    public List<User> loginAndGetAllUsers(String email, String password) {
//        User loggedInUser = loginUser(email, password);  // Korzysta z funkcji loginUser do logowania
//        if (loggedInUser != null) {
//            return userRepository.findAll();  // Zwraca wszystkich użytkowników po udanym logowaniu
//        } else {
//            throw new IllegalArgumentException("Nie można zalogować użytkownika.");
//        }
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User user) {
        try {
            User updatedUser = userService.updateUser(id, user);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{userId}/favorite/{movieId}")
    public ResponseEntity<User> addFavoriteMovie(@PathVariable String userId, @PathVariable String movieId) {
        try {
            User updatedUser = userService.addFavoriteMovie(userId, movieId);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    @PutMapping("/{userId}/watched/{movieId}")
    public ResponseEntity<User> addWatchedMovie(@PathVariable String userId, @PathVariable String movieId) {
        try {
            User updatedUser = userService.addWatchedMovie(userId, movieId);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


    @GetMapping("/admin")
    public String endpointForAdmin(String email, String password) {

        return "This should be only visible by admin";
    }
}
