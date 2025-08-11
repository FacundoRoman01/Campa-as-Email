package com.campana.email.email_campaigner_api.dto;

import java.util.List;

public class AuthResponseDto {

    private String accessToken;
    private String tokenType = "Bearer ";
    private Long userId;
    private String email;
    private List<String> roles;

    // Este es el único constructor que necesitas
    public AuthResponseDto(String accessToken, Long userId, String email, List<String> roles) {
        this.accessToken = accessToken;
        this.userId = userId;
        this.email = email;
        this.roles = roles;
    }

    // Getters y Setters
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getRoles() {
        return roles;
    }
}