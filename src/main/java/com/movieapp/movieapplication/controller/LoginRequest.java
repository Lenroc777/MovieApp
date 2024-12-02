package com.movieapp.movieapplication.controller;

public class LoginRequest {
    private String email;
    private String password;

    // Konstruktor domyślny
    public LoginRequest() {
    }

    // Konstruktor z argumentami
    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Gettery i settery
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
