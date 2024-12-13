package com.LMS.LMS.Services;

import com.LMS.LMS.Controllers.ApiResponses.APIResponse;
import com.LMS.LMS.Controllers.ApiResponses.AuthenticationResponse;
import com.LMS.LMS.Controllers.ControllerParams.LoginParams;
import com.LMS.LMS.Controllers.ControllerParams.RegisterParams;
import com.LMS.LMS.Models.Role;
import com.LMS.LMS.Models.User;
import com.LMS.LMS.Repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                                 JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse login(LoginParams loginParams) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginParams.email, loginParams.password)
        );
        return userRepository.findByEmail(loginParams.email)
                .map(user -> {
                    String token = jwtService.GenerateJwtToken(user);
                    return new AuthenticationResponse(200, token);
                })
                .orElseThrow(() -> new IllegalArgumentException("Invalid Email Or Password"));
    }


    public APIResponse Register(RegisterParams registerParams) {
        if (userRepository.findByEmail(registerParams.email).isPresent()) {
            throw new IllegalArgumentException("Email is already in use");
        }
        var user = new User();
        user.setEmail(registerParams.email);
        user.setName(registerParams.name);
        user.setPassword(passwordEncoder.encode(registerParams.password));
        user.setRole(Role.ROLE_Student);
        try {
            userRepository.save(user);
            String token = jwtService.GenerateJwtToken(user);
            return new AuthenticationResponse(201, token);
        } catch (Exception e) {
            throw new IllegalStateException("Registration failed");
        }
    }
}
