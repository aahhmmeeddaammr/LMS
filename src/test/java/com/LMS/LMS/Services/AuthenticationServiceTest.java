package com.LMS.LMS.Services;

import com.LMS.LMS.Controllers.ApiResponses.APIResponse;
import com.LMS.LMS.Controllers.ApiResponses.AuthenticationResponse;
import com.LMS.LMS.Controllers.ControllerParams.CompleteProfileParams;
import com.LMS.LMS.Controllers.ControllerParams.LoginParams;
import com.LMS.LMS.Controllers.ControllerParams.RegisterParams;
import com.LMS.LMS.Models.Student;
import com.LMS.LMS.Models.User;
import com.LMS.LMS.Repositories.AdminRepository;
import com.LMS.LMS.Repositories.InstructorRepository;
import com.LMS.LMS.Repositories.StudentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {
    @InjectMocks
    private AuthenticationService authenticationService;
    @Mock
    private AdminRepository adminRepository;
    @Mock
    private InstructorRepository instructorRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void login() {
        LoginParams loginParams = new LoginParams();
        loginParams.email = "enas@mail.com";
        loginParams.password = "enas";
        User user = new Student();
        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(studentRepository.findByEmail(loginParams.email)).thenReturn(Optional.of(user));
        when(jwtService.GenerateJwtToken(user)).thenReturn("Token");
        AuthenticationResponse response = new AuthenticationService(
                adminRepository, instructorRepository, studentRepository,
                passwordEncoder, jwtService, authenticationManager
        ).login(loginParams);

        assertNotNull(response);
        assertEquals(200, response.status);
        assertEquals("Token", response.token);
    }


    @Test
    void register() {
        RegisterParams registerParams = new RegisterParams();
        registerParams.email = "enas@mail.com";
        registerParams.password = "password";
        registerParams.name = "Enas";
        registerParams.role = "Student";

        when(studentRepository.findByEmail(registerParams.email)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registerParams.password)).thenReturn("encodedPassword");
        when(jwtService.GenerateJwtToken(any())).thenReturn("Token");

        AuthenticationService service = new AuthenticationService(
                adminRepository, instructorRepository, studentRepository,
                passwordEncoder, jwtService, authenticationManager
        );

        AuthenticationResponse response = (AuthenticationResponse) service.Register(registerParams);
        assertNotNull(response);
        assertEquals(201, response.status);
        assertEquals("Token", response.token);
    }


    @Test
    void completeProfile() {
        CompleteProfileParams profileParams = new CompleteProfileParams();
        profileParams.phone = "01018309487";
        profileParams.Address = "badrasheen";
        User user = new Student();
        user.setEmail("enas@mail.com");
        when(studentRepository.findByEmail(user.getEmail())).thenReturn(Optional.of((Student) user));
        AuthenticationService service = new AuthenticationService(
                adminRepository, instructorRepository, studentRepository,
                passwordEncoder, jwtService, authenticationManager
        );

        APIResponse response = service.completeProfile(user.getEmail(), profileParams);
        assertNotNull(response);
        assertEquals(200, response.status);
        verify(studentRepository, times(1)).save(any(Student.class));
    }


    @Test
    void viewProfile() {
        User user = new Student();
        user.setEmail("enas@mail.com");
        when(studentRepository.findByEmail(user.getEmail())).thenReturn(Optional.of((Student) user));
        AuthenticationService service = new AuthenticationService(
                adminRepository, instructorRepository, studentRepository,
                passwordEncoder, jwtService, authenticationManager
        );
        APIResponse response = service.viewProfile(user.getEmail());

        assertNotNull(response);
        assertEquals(200, response.status);
    }

    @Test
    void updateProfile() {
        CompleteProfileParams profileParams = new CompleteProfileParams();
        profileParams.phone = "01018309487";
        profileParams.Address = "badrasheen";
        User user = new Student();
        user.setEmail("enas@mail.com");
        when(studentRepository.findByEmail(user.getEmail())).thenReturn(Optional.of((Student) user));
        AuthenticationService service = new AuthenticationService(
                adminRepository, instructorRepository, studentRepository,
                passwordEncoder, jwtService, authenticationManager
        );
        APIResponse response = service.updateProfile(user.getEmail(), profileParams);

        assertNotNull(response);
        assertEquals(200, response.status);
        verify(studentRepository, times(1)).save(any(Student.class));
    }

}