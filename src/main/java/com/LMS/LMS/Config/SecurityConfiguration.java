package com.LMS.LMS.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtFilter;
    private final AuthenticationProvider authenticationProvider;

    public SecurityConfiguration(JwtAuthenticationFilter jwtFilter, AuthenticationProvider authenticationProvider) {
        this.jwtFilter = jwtFilter;
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/uploads/**").permitAll()
                        .requestMatchers("/api/v1/auth/register").hasRole("ROLE_ADMIN")
                        .requestMatchers("/api/v1/auth/login").permitAll()
                        .requestMatchers("/api/v1/course/add-course").hasRole("ROLE_Instructor")
                        .requestMatchers("/api/v1/course/add-lesson").hasRole("ROLE_Instructor")
                        .requestMatchers("/api/v1/course/enroll").hasRole("ROLE_Student")
                        .requestMatchers("/api/v1/course/attend-lesson").hasRole("ROLE_Student")
                        .requestMatchers("/api/v1/course/add-material/**").hasRole("ROLE_Instructor")
                        .requestMatchers("/api/v1/assessment/submit-quiz/{id}").hasRole("ROLE_Student")
                        .requestMatchers("/api/v1/course/delete/{sid}/{cid}").hasRole("ROLE_Instructor")
                        .requestMatchers("/api/v1/assessment/add-questions/{id}").hasRole("ROLE_Instructor")
                        .requestMatchers("/api/v1/assessment/generate-quiz/{id}").hasRole("ROLE_Instructor")
                        .requestMatchers("/api/v1/assessment/add-assignment/{id}").hasRole("ROLE_Instructor")
                        .requestMatchers("/api/v1/assessment/submit-assignment/{id}").hasRole("ROLE_Student")
                        .requestMatchers("/api/v1/assessment/get-student-assignment").hasRole("ROLE_Instructor")
                        .requestMatchers("/api/v1/assessment/get-students-assignments/{id}").hasRole("ROLE_Instructor")
                        .requestMatchers("/api/v1/assessment/correct-assignment").hasRole("ROLE_Instructor")
                        .requestMatchers("/api/v1/performance/**").hasAnyRole("ROLE_ADMIN", "ROLE_Instructor")
                        .requestMatchers("/api/v1/assessment/get-all-quiz-grades/{id}").hasRole("ROLE_Instructor")
                        .requestMatchers("/api/v1/assessment/get-all-quizzes-for-student/{id}").hasRole("ROLE_Instructor")
                        .requestMatchers("/api/v1/assessment/get-all-grades/{cid}/{sid}").hasRole("ROLE_Instructor")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
