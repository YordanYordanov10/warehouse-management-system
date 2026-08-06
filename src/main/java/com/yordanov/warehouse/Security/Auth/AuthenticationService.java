package com.yordanov.warehouse.Security.Auth;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.yordanov.warehouse.Exception.ConflictException;
import com.yordanov.warehouse.Security.Jwt.JwtService;
import com.yordanov.warehouse.User.Model.User;
import com.yordanov.warehouse.User.Model.UserRole;
import com.yordanov.warehouse.User.Repository.UserRepository;
import com.yordanov.warehouse.Web.Dto.AuthenticationRequest;
import com.yordanov.warehouse.Web.Dto.AuthenticationResponse;
import com.yordanov.warehouse.Web.Dto.RegisterRequest;

import jakarta.transaction.Transactional;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {

        Optional<User> existingUser = userRepository.findByUsernameOrEmail(request.getUsername(), request.getEmail());

        if (existingUser.isPresent()) {
            throw new ConflictException("Username or email already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .isActive(true)
                .role(UserRole.USER)
                .build();

        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user.getUsername());
        return  AuthenticationResponse.builder()
                .token(jwtToken)
                .message("User registered successfully")
                .build();
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String jwtToken = jwtService.generateToken(user.getUsername());
        return  AuthenticationResponse.builder()
                .token(jwtToken)
                .message("User authenticated successfully")
                .build();
    }
}
