package com.example.RestaurantSecurity.Controller;

import com.example.RestaurantSecurity.DTO.LoginRequest;
import com.example.RestaurantSecurity.DTO.LoginResponse;
import com.example.RestaurantSecurity.DTO.RegisterRequest;
import com.example.RestaurantSecurity.Service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authService;
    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(
            @RequestBody RegisterRequest request){
        return ResponseEntity.ok(authService.register(request));
    }
    @PostMapping("/authenticate")
    public ResponseEntity<LoginResponse> register(
            @RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.authenticate(request));
    }

}
