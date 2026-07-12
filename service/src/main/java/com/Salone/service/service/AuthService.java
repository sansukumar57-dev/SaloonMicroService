package com.Salone.service.service;

import com.Salone.service.payload.dto.SignupDTO;
import com.Salone.service.payload.response.AuthResponse;

public interface AuthService {
    AuthResponse login(String username, String password) throws Exception;
    AuthResponse signup(SignupDTO req) throws Exception;
    AuthResponse getAccessTokenRefreshToken(String refreshToken) throws Exception;
}
