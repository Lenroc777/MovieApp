package com.movieapp.movieapplication.controller;

public class RegisterRequest {
    private String username;
    private String email;
    private String password;

    // Konstruktor domyślny
    public RegisterRequest() {
    }

    // Konstruktor z argumentami
    public RegisterRequest(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Gettery i settery
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
