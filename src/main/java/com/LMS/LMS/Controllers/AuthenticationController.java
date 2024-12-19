package com.LMS.LMS.Controllers;

import com.LMS.LMS.Controllers.ApiResponses.APIResponse;
import com.LMS.LMS.Controllers.ApiResponses.GetResponse;
import com.LMS.LMS.Controllers.ControllerParams.CompleteProfileParams;
import com.LMS.LMS.Controllers.ControllerParams.LoginParams;
import com.LMS.LMS.Controllers.ControllerParams.RegisterParams;
import com.LMS.LMS.DTOs.ProfileDTO;
import com.LMS.LMS.Services.AuthenticationService;
import com.LMS.LMS.Services.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
public class AuthenticationController {

    private final JwtService jwtService;
    AuthenticationService authenticationService;
    public AuthenticationController(AuthenticationService authenticationService, JwtService jwtService) {
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
    }
    @PostMapping("/login")
    public ResponseEntity<APIResponse> Login(@RequestBody LoginParams User) {
        System.out.println("getAllCourses() endpoint hit");
        return ResponseEntity.ok(authenticationService.login(User));
    }
    @PostMapping("/register")
    public ResponseEntity<APIResponse> Register(@RequestBody RegisterParams User) {
        return ResponseEntity.ok(authenticationService.Register(User));
    }

    @PostMapping("/complete-profile")
    public ResponseEntity<APIResponse> completeProfile(@RequestBody CompleteProfileParams params, @RequestHeader String Authorization){
        String token = Authorization.substring(7);
        var Claims = jwtService.ExtractClaimsFromJWT(token);
        String email = Claims.get("email").toString(); // Id of User want to complete profile
        return ResponseEntity.ok(authenticationService.completeProfile(email, params));
    }
    @GetMapping("/view-profile")
    public ResponseEntity<APIResponse> viewProfile(@RequestHeader String Authorization){
        String token = Authorization.substring(7);
        var Claims = jwtService.ExtractClaimsFromJWT(token);
        String email = Claims.get("email").toString();
        return ResponseEntity.ok(authenticationService.viewProfile(email));
    }
    @PutMapping("/update-profile")
    public ResponseEntity<APIResponse> updateProfile(@RequestBody CompleteProfileParams params, @RequestHeader String Authorization){
        String token = Authorization.substring(7);
        var Claims = jwtService.ExtractClaimsFromJWT(token);
        String email = Claims.get("email").toString();
        return ResponseEntity.ok(authenticationService.updateProfile(email, params));
    }
}