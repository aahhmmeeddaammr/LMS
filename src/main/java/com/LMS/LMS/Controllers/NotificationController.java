package com.LMS.LMS.Controllers;

import com.LMS.LMS.Controllers.ApiResponses.APIResponse;
import com.LMS.LMS.Services.JwtService;
import com.LMS.LMS.Services.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/notification")
public class NotificationController{

    private final NotificationService notificationService;
    private final JwtService jwtService;

    public NotificationController(NotificationService notificationService, JwtService jwtService) {
        this.notificationService = notificationService;
        this.jwtService = jwtService;
    }

    @GetMapping("/get-unread-notifications")
    public ResponseEntity<APIResponse> getUnreadNotifications(@RequestHeader String Authorization) {
        String token = Authorization.substring(7);
        var claims = jwtService.ExtractClaimsFromJWT(token);
        int id = claims.get("id", Integer.class);
        String role = claims.get("role", String.class);
        return ResponseEntity.ok(notificationService.getUnReadNotifications(id, role));
    }

    @GetMapping("/get-all-notifications")
    public ResponseEntity<APIResponse> getAllNotifications(@RequestHeader String Authorization){
        String token = Authorization.substring(7);
        var claims = jwtService.ExtractClaimsFromJWT(token);
        int id = claims.get("id", Integer.class);
        String role = claims.get("role", String.class);
        return ResponseEntity.ok(notificationService.getAllNotifications(id, role));
    }
}