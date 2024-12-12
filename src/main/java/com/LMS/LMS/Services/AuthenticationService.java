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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    UserRepository userRepository  ;
    PasswordEncoder passwordEncoder;
    JwtService jwtService;
    AuthenticationManager authenticationManager;
    public AuthenticationService(UserRepository userRepository , PasswordEncoder passwordEncoder , JwtService jwtService , AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public APIResponse login(LoginParams loginParams) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginParams.email, loginParams.password));
        Optional<User> user = userRepository.findByEmail(loginParams.email);
        User newuser = user.orElse(null);
        if (newuser != null) {
            String Token = jwtService.GenerateJwtToken(newuser);
            return new AuthenticationResponse(200 , Token);
        }
        return new AuthenticationResponse(401, "Invalid email or password");
    }

    public APIResponse Register(RegisterParams registerParams) {
       try {
           var user = new User();
           user.setEmail(registerParams.email);
           user.setName(registerParams.name);
           user.setPassword( passwordEncoder.encode(registerParams.password));
           user.setRole(Role.ROLE_Student);
           userRepository.save(user);
           String Token = jwtService.GenerateJwtToken(user);
           return new AuthenticationResponse(201, Token);
       }catch (Exception e) {
           e.printStackTrace();
           return new AuthenticationResponse(500, "");
       }
    }

}

