package com.smartorder.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class LogoutController {
    private final AuthService authService;

    public LogoutController(AuthService authService) {
        this.authService = authService;
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @RequestHeader("Authorization") String header
    ){
        if (header!=null && header.startsWith("Bearer ")){
            String token=header.substring(7);
            authService.logout(token);
        }
        return ResponseEntity.ok("Logged out successfully");
    }
}
