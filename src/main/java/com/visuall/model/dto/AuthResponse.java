package com.visuall.model.dto;

public class AuthResponse {
    private String token;
    private UserDTO user;

    // getters e setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public UserDTO getUser() { return user; }
    public void setUser(UserDTO user) { this.user = user; }
}