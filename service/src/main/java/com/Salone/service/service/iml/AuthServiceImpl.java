package com.Salone.service.service.iml;

import com.Salone.service.model.User;
import com.Salone.service.payload.dto.SignupDTO;
import com.Salone.service.payload.response.AuthResponse;
import com.Salone.service.payload.response.TokenResponse;
import com.Salone.service.repository.UserRepository;
import com.Salone.service.service.AuthService;
import com.Salone.service.service.KeycloackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final KeycloackService keycloackService;


    @Override
    public AuthResponse login(String username, String password) throws Exception {
        TokenResponse tokenResponse=keycloackService.getAdminAccessToken(
               username
                ,password,
                "password",null);

        AuthResponse authResponse=new AuthResponse();
        authResponse.setRefresh_token(tokenResponse.getRefreshToken());
        authResponse.setJwt(tokenResponse.getAccessToken());
        authResponse.setMessage("Login successfully");
        return authResponse;
    }

    @Override
    public AuthResponse signup(SignupDTO req) throws Exception {
        keycloackService.createUser(req);

        User user =new User();
        user.setUsername(req.getUsername());
        user.setPassword(req.getPassword());
        user.setEmail(req.getEmail());
        user.setRole(req.getRole());
        user.setFullName(req.getFullName());
        user.setCreateAt(LocalDateTime.now());
        userRepository.save(user);


        TokenResponse tokenResponse=keycloackService.getAdminAccessToken(
                req.getUsername()
                ,req.getPassword(),
        "password",null);

        AuthResponse authResponse=new AuthResponse();
        authResponse.setRefresh_token(tokenResponse.getRefreshToken());
        authResponse.setJwt(tokenResponse.getAccessToken());
        authResponse.setRole(user.getRole());
        authResponse.setMessage("Registered successfully");
        return authResponse;
    }

    @Override
    public AuthResponse getAccessTokenRefreshToken(String refreshToken) throws Exception {
        TokenResponse tokenResponse=keycloackService.getAdminAccessToken(
                null
                ,null,
                "refresh_token",refreshToken);

        AuthResponse authResponse=new AuthResponse();
        authResponse.setRefresh_token(tokenResponse.getRefreshToken());
        authResponse.setJwt(tokenResponse.getAccessToken());
        authResponse.setMessage("Login success");
        return authResponse;
    }
}
