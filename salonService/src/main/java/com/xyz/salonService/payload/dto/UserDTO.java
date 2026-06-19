package com.xyz.salonService.payload.dto;

import lombok.Data;

@Data
public class UserDTO {
    private long id;
    private String fullName;
    private String email;
}
