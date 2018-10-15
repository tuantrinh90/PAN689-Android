package com.football.models.requests;

public class LoginRequest {
    private String email;
    private String password;
    private String deviceToken;
    private String code;

    public LoginRequest() {
    }

    public LoginRequest(String email, String password, String deviceToken, String code) {
        this.email = email;
        this.password = password;
        this.deviceToken = deviceToken;
        this.code = code;
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

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", deviceToken='" + deviceToken + '\'' +
                '}';
    }
}
