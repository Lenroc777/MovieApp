package com.movieapp.movieapplication.service;

import com.movieapp.movieapplication.model.Movie;
import com.movieapp.movieapplication.model.User;
import com.movieapp.movieapplication.repository.MovieRepository;
import com.movieapp.movieapplication.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final MovieRepository movieRepository;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, EmailService emailService, MovieRepository movieRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.movieRepository = movieRepository;
    }


    public User registerUser(String username, String email, String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email is already in use.");
        }
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username is already in use.");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setRole("ROLE_USER");

        return userRepository.save(user);
    }

    public User loginUser(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("Invalid email or password.");
        }

        User user = userOptional.get();
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid email or password.");
        }

        return user;
    }

    public List<User> getAllUsers() {

        return userRepository.findAll();
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    public User updateUser(String id, User user) {
        return userRepository.findById(id).map(existingUser -> {
            existingUser.setUsername(user.getUsername());
            existingUser.setEmail(user.getEmail());
            existingUser.setRole(user.getRole());
            // Jeśli hasło jest zmieniane, zakoduj je:
            if (user.getPasswordHash() != null && !user.getPasswordHash().isEmpty()) {
                existingUser.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
            }
            return userRepository.save(existingUser);
        }).orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
    }


    public void deleteUser(String id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("User not found with id: " + id);
        }
    }

    public User addFavoriteMovie(String userId, String movieId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (!user.getFavoriteMovies().contains(movieId)) {
            user.getFavoriteMovies().add(movieId);
            userRepository.save(user);
        }
        return user;
    }
    public User addWatchedMovie(String userId, String movieId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(()-> new IllegalArgumentException("Movie not found"));
        emailService.sendEmail(user.getEmail(), "Dodano do ulubionych", "Film " + movie.getTitle() + " został dodany do Twoich ulubionych.");
        if (!user.getFavoriteMovies().contains(movieId)) {
            user.getFavoriteMovies().add(movieId);
            userRepository.save(user);
        }
        emailService.sendEmail(user.getEmail(), "Dodano do ulubionych", "Film " + movie.getTitle() + " został dodany do Twoich ulubionych.");
        return user;
    }


}