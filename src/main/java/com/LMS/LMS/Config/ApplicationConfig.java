package com.LMS.LMS.Config;

import com.LMS.LMS.Models.User;
import com.LMS.LMS.Repositories.AdminRepository;
import com.LMS.LMS.Repositories.InstructorRepository;
import com.LMS.LMS.Repositories.StudentRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
public class ApplicationConfig {

    private final AdminRepository adminRepository;
    private final InstructorRepository instructorRepository;
    private final StudentRepository studentRepository;
    public ApplicationConfig( AdminRepository adminRepository, InstructorRepository instructorRepository, StudentRepository studentRepository) {
        this.adminRepository = adminRepository;
        this.instructorRepository = instructorRepository;
        this.studentRepository = studentRepository;
    }
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            try {
                return (UserDetails) findUserByEmail(username).orElseThrow(() -> new IllegalAccessException());
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Invalid Email");
            }
        };
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
    @Bean
    public AuthenticationManager authenticationManager( AuthenticationConfiguration authenticationConfiguration) throws  Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("");
    }

    private Optional<User> findUserByEmail(String email) {
        Optional<User> user = studentRepository.findByEmail(email);
        if (user.isPresent()) return user;
        user = instructorRepository.findByEmail(email);
        if (user.isPresent()) return user;
        return adminRepository.findByEmail(email);
    }
}
