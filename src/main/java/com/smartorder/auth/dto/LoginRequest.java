package com.smartorder.auth.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    @NotBlank
    private String username;
    @NotBlank
    private String password;

    public LoginRequest(String password, String username) {
        this.password = password;
        this.username = username;
    }

    public LoginRequest() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
