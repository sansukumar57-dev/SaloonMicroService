package com.Salone.service.payload.dto;

import com.zaxxer.hikari.util.Credentials;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserRequest {
    private String username;
    private Boolean enabled;
    private String firstName;
    private String lastName;
    private String email;
    private List<Credential> credential=new ArrayList<>();

}
