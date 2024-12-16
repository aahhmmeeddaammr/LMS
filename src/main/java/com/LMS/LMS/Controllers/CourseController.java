package com.LMS.LMS.Controllers;

import com.LMS.LMS.Controllers.ApiResponses.APIResponse;
import com.LMS.LMS.Controllers.ControllerParams.AddCourseParams;
import com.LMS.LMS.Controllers.ControllerParams.AddLessonParams;
import com.LMS.LMS.Controllers.ControllerParams.AttendLessonParams;
import com.LMS.LMS.Controllers.ControllerParams.EnrollParams;
import com.LMS.LMS.Models.Course;
import com.LMS.LMS.Services.CourseService;
import com.LMS.LMS.Services.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("api/v1/course")
public class CourseController {

    CourseService courseService;
    JwtService jwtService;

    public CourseController(CourseService courseService, JwtService jwtService) {
        this.courseService = courseService;
        this.jwtService = jwtService;
    }

    @PostMapping("/add-course")
    public ResponseEntity<APIResponse> addCourse(@RequestBody AddCourseParams course, @RequestHeader String Authorization) {
        String token = Authorization.substring(7);
        var Claims = jwtService.ExtractClaimsFromJWT(token);
        int ID = Claims.get("id", Integer.class);
        return ResponseEntity.ok(courseService.addCourse(course, ID));
    }

    @GetMapping
    public ResponseEntity<APIResponse> getAllCourses(@RequestHeader String Authorization) {
        String token = Authorization.substring(7);
        var Claims = jwtService.ExtractClaimsFromJWT(token);
        String role = Claims.get("role", String.class);
        return ResponseEntity.ok(courseService.getAllCourses(role));
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse> getCourseById(@PathVariable int id, @RequestHeader String Authorization) {
        String token = Authorization.substring(7);
        var Claims = jwtService.ExtractClaimsFromJWT(token);
        String role = Claims.get("role", String.class);
        return ResponseEntity.ok(courseService.getCourseById(id, role));
    }

    @PostMapping("/enroll")
    public ResponseEntity<APIResponse> enrollCourse(@RequestBody EnrollParams params, @RequestHeader String Authorization) {
        String token = Authorization.substring(7);
        var Claims = jwtService.ExtractClaimsFromJWT(token);
        int ID = Claims.get("id", Integer.class);
        return ResponseEntity.ok(courseService.enrollStudent(params.getCourse_id(), ID));
    }

    @PostMapping("/add-lesson")
    public ResponseEntity<APIResponse> addLesson(@RequestBody AddLessonParams params) throws Exception {
        return ResponseEntity.ok(courseService.addLesson(params));
    }

    @PostMapping("/attend-lesson")
    public ResponseEntity<APIResponse> attendLesson(@RequestBody AttendLessonParams params, @RequestHeader String Authorization) throws Exception {
        String token = Authorization.substring(7);
        var Claims = jwtService.ExtractClaimsFromJWT(token);
        int ID = Claims.get("id", Integer.class);
        return ResponseEntity.ok(courseService.attendLesson(params, ID));
    }
    @PostMapping("/add-material/{id}")
    public ResponseEntity<APIResponse> addMaterial(@PathVariable int id,@RequestParam("file") MultipartFile file ) throws Exception {
        return ResponseEntity.ok(courseService.addMaterial(file, id));
    }
}
