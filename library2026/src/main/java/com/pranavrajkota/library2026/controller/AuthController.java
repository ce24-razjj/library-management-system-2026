package com.pranavrajkota.library2026.controller;

import com.pranavrajkota.library2026.dto.AuthResponse;
import com.pranavrajkota.library2026.dto.LoginRequest;
import com.pranavrajkota.library2026.dto.UserDto;
import com.pranavrajkota.library2026.entity.User;
import com.pranavrajkota.library2026.repository.UserRepository;
import com.pranavrajkota.library2026.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }


    @PostMapping("/register")
    public String register(@RequestBody UserDto userDto){
        User user = new User();
        user.setUserName(userDto.getUsername());

        String strongPassword = passwordEncoder.encode(userDto.getPassword());
        user.setPassword(strongPassword);

        user.setRole(userDto.getRole());

//        user.setRole("USER");
        userRepository.save(user);
        return "User registered successfully with hashed password!";
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {

        //
        System.out.println("INPUT PASS: " + request.getPassword());

        User user = userRepository.findByUserName(request.getUsername()).orElse(null);
        if(user != null){
            System.out.println("DB PASS: " + user.getPassword());
            System.out.println("MATCH: " + passwordEncoder.matches(request.getPassword(), user.getPassword()));
        }
        //

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        return jwtService.generateToken(request.getUsername());
    }
}
