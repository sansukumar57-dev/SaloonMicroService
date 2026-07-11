package com.Salone.service.payload.dto;

import lombok.Data;

@Data
public class SignupDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String username;
    private String role;

}