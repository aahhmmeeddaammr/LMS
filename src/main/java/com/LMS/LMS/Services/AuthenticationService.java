package com.LMS.LMS.Services;

import com.LMS.LMS.Controllers.ApiResponses.APIResponse;
import com.LMS.LMS.Controllers.ApiResponses.AuthenticationResponse;
import com.LMS.LMS.Controllers.ControllerParams.LoginParams;
import com.LMS.LMS.Controllers.ControllerParams.RegisterParams;
import com.LMS.LMS.Models.*;
import com.LMS.LMS.Repositories.AdminRepository;
import com.LMS.LMS.Repositories.InstructorRepository;
import com.LMS.LMS.Repositories.StudentRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class AuthenticationService {

    private final AdminRepository adminRepository;
    private final InstructorRepository instructorRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(AdminRepository adminRepository, com.LMS.LMS.Repositories.InstructorRepository instructorRepository, StudentRepository studentRepository, PasswordEncoder passwordEncoder,
                                 JwtService jwtService, AuthenticationManager authenticationManager) {
        this.adminRepository = adminRepository;
        this.instructorRepository = instructorRepository;
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse login(LoginParams loginParams) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginParams.email, loginParams.password)
        );
        Optional<User> user = findUserByEmail(loginParams.email);
        return user.map(user2 -> {
                    String token = jwtService.GenerateJwtToken(user2);
                    return new AuthenticationResponse(200, token);
                })
                .orElseThrow(() -> new IllegalArgumentException("Invalid Email Or Password"));
    }


    public APIResponse Register(RegisterParams registerParams) {
        try {
            Optional<User> userFind = findUserByEmail(registerParams.email);
        if (userFind.isPresent()) {
            throw new IllegalArgumentException("Email is already in use");
        }
        User user;

        if(registerParams.role.equals("Student")){
            user= new Student();
        }else if(registerParams.role.equals("Instructor")){
            user= new Instructor();
        }else if(registerParams.role.equals("Admin")){
            user = new Admin();
        }else{
            throw new IllegalArgumentException("Invalid Role");
        }
            user.setEmail(registerParams.email);
            user.setName(registerParams.name);
            user.setPassword(passwordEncoder.encode(registerParams.password));
            saveUser(user);
            String token = jwtService.GenerateJwtToken(user);
            return new AuthenticationResponse(201, token);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }


    private Optional<User> findUserByEmail(String email) {
        Optional<User> user = studentRepository.findByEmail(email);
        if (user.isPresent()) return user;
        user = instructorRepository.findByEmail(email);
        if (user.isPresent()) return user;
        return adminRepository.findByEmail(email);
    }
    private void saveUser(User user) {
        if (user instanceof Student) {
            studentRepository.save((Student) user);
        } else if (user instanceof Instructor) {
            instructorRepository.save((Instructor) user);
        } else if (user instanceof Admin) {
            adminRepository.save((Admin) user);
        }
    }
}
