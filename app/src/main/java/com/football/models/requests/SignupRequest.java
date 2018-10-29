package com.football.models.requests;

public class SignupRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String passwordConfirm;
    private String code;

    public SignupRequest(String firstName, String lastName, String email, String password, String passwordConfirm, String code) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.code = code;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public String getCode() {
        return code;
    }
}
