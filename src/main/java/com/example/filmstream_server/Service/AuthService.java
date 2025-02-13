package com.example.filmstream_server.Service;

import com.example.filmstream_server.Model.User;
import com.example.filmstream_server.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final Map<String, Integer> otpStorage = new HashMap<>();

    public Map<String, Object> login(String email, String password) {
        Optional<User> user = userRepository.findByUsername(email);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            return Map.of("status", "success", "message", "Successfully logged in", "user_id", user.get().getId());
        }
        return Map.of("status", "error", "message", "Invalid credentials");
    }

    public Map<String, Object> sendOtpForRegister(String email) {
        if (userRepository.findByUsername(email).isPresent()) {
            return Map.of("status", "error", "message", "Email already exists");
        }

        int otp = new Random().nextInt(900000) + 100000;
        otpStorage.put(email, otp);
        emailService.sendOtpEmail(email, otp);

        return Map.of("status", "success", "message", "OTP sent successfully");
    }

    public Map<String, Object> createAccount(String email, String name, String password) {
        if (userRepository.findByUsername(email).isPresent()) {
            return Map.of("status", "error", "message", "Username already exists");
        }

        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(name, email, encodedPassword);
        userRepository.save(user);

        return Map.of("status", "success", "message", "Account created", "user_id", user.getId());
    }

    public Map<String, Object> forgotPassword(String email) {
        if (userRepository.findByUsername(email).isEmpty()) {
            return Map.of("status", "error", "message", "Email not found");
        }

        int otp = new Random().nextInt(900000) + 100000;
        otpStorage.put(email, otp);
        emailService.sendOtpEmail(email, otp);

        return Map.of("status", "success", "message", "OTP sent successfully");
    }

    public Map<String, Object> resetPassword(String email, String password) {
        Optional<User> userOpt = userRepository.findByUsername(email);
        if (userOpt.isEmpty()) {
            return Map.of("status", "error", "message", "Email not found");
        }

        User user = userOpt.get();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        otpStorage.remove(email);
        return Map.of("status", "success", "message", "Password updated successfully");
    }
}
