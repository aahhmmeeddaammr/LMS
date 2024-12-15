package com.LMS.LMS.Controllers;

import com.LMS.LMS.Controllers.ApiResponses.APIResponse;
import com.LMS.LMS.Controllers.ControllerParams.AddCourseParams;
import com.LMS.LMS.Models.Course;
import com.LMS.LMS.Services.CourseService;
import com.LMS.LMS.Services.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/course")
public class CourseController {

    CourseService courseService;
    JwtService jwtService;
    public CourseController(CourseService courseService , JwtService jwtService) {
        this.courseService = courseService;
        this.jwtService = jwtService;
    }

    @PostMapping("/add-course")
    public ResponseEntity<APIResponse> addCourse(@RequestBody AddCourseParams course , @RequestHeader String Authorization) {
        String token = Authorization.substring(7);
        var Claims=jwtService.ExtractClaimsFromJWT(token);
        int ID = Claims.get("id", Integer.class);
        return ResponseEntity.ok(courseService.addCourse(course ,ID));
    }

    @GetMapping
    public ResponseEntity<APIResponse> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }
}
