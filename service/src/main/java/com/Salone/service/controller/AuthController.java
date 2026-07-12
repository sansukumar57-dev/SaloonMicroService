package com.Salone.service.controller;


import com.Salone.service.payload.dto.LoginDTO;
import com.Salone.service.payload.dto.SignupDTO;
import com.Salone.service.payload.response.AuthResponse;
import com.Salone.service.service.AuthService;
import com.Salone.service.service.KeycloackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class AuthController {
    private final AuthService authService;


    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(
            @RequestBody SignupDTO req
            ) throws Exception {
    AuthResponse res=authService.signup(req);
    return ResponseEntity.ok(res);

    }



    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginDTO req
    ) throws Exception {
        AuthResponse res=authService.login(req.getEmail(),req.getPassword());
        return ResponseEntity.ok(res);

    }


    @GetMapping("/access-token/refresh-token/{refreshToken}")
    public ResponseEntity<AuthResponse> getAccessToken(
            @PathVariable String refreshToken
    ) throws Exception {
        AuthResponse res=authService.getAccessTokenRefreshToken(refreshToken);
        return ResponseEntity.ok(res);

    }
}
