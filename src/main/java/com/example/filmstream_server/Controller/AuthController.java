package com.example.filmstream_server.Controller;

import com.example.filmstream_server.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(authService.login(request.get("username"), request.get("password")));
    }

    @PostMapping("/registers")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(authService.sendOtpForRegister(request.get("email")));
    }

    @PostMapping("/create-account")
    public ResponseEntity<?> createAccount(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(authService.createAccount(request.get("email"), request.get("name"), request.get("password")));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(authService.forgotPassword(request.get("email")));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(authService.resetPassword(request.get("email"), request.get("password")));
    }
}
