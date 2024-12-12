package com.LMS.LMS.Controllers;

import com.LMS.LMS.Controllers.ApiResponses.APIResponse;
import com.LMS.LMS.Controllers.ApiResponses.AuthenticationResponse;
import com.LMS.LMS.Controllers.ControllerParams.LoginParams;
import com.LMS.LMS.Controllers.ControllerParams.RegisterParams;
import com.LMS.LMS.Services.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
public class AuthenticationController {

    AuthenticationService authenticationService;
    public AuthenticationController( AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
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
}
