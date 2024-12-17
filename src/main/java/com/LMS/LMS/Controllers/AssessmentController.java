package com.LMS.LMS.Controllers;

import com.LMS.LMS.Controllers.ApiResponses.APIResponse;
import com.LMS.LMS.Controllers.ApiResponses.GetResponse;
import com.LMS.LMS.Controllers.ControllerParams.AddAssignmentParams;
import com.LMS.LMS.Controllers.ControllerParams.AddQuestionsParams;
import com.LMS.LMS.Controllers.ControllerParams.AddQuizParams;
import com.LMS.LMS.Controllers.ControllerParams.SubmitQuizParams;
import com.LMS.LMS.Services.AssessmentService;
import com.LMS.LMS.Services.JwtService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@RestController
@RequestMapping("api/v1/assessment")
public class AssessmentController {
    private final AssessmentService assessmentService;
    private final JwtService jwtService;

    public AssessmentController(AssessmentService assessmentService, JwtService jwtService) {
        this.assessmentService = assessmentService;
        this.jwtService = jwtService;
    }

    @PostMapping("/add-questions/{id}")
    public ResponseEntity<APIResponse> addQuestion(@RequestBody List<AddQuestionsParams> params, @PathVariable int id) {
        return ResponseEntity.ok(assessmentService.createQuestionBank(params, id));
    }

    @PostMapping("/generate-quiz/{id}")
    public ResponseEntity<APIResponse> createQuiz(@RequestBody AddQuizParams params, @PathVariable int id) {
        return ResponseEntity.ok(assessmentService.createQuiz(params, id));
    }

    @GetMapping("/get-quiz/{id}")
    public ResponseEntity<APIResponse> getQuiz(@PathVariable int id) {
        return ResponseEntity.ok(assessmentService.getQuiz(id));
    }

    @PostMapping("/submit-quiz/{id}")
    public ResponseEntity<APIResponse> submitQuiz(@RequestBody SubmitQuizParams params, @RequestHeader String Authorization, @PathVariable int id) {
        String token = Authorization.substring(7);
        var Claims = jwtService.ExtractClaimsFromJWT(token);
        int stdID = Claims.get("id", Integer.class);
        return ResponseEntity.ok(assessmentService.submitQuiz(params, id, stdID));
    }

    @PostMapping(value = "/add-assignment/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<APIResponse> addAssignment(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("grade") double grade,
            @RequestParam("deadline") String deadline,
            @RequestParam("files") List<MultipartFile> files,
            @PathVariable int id) {

        AddAssignmentParams params = new AddAssignmentParams();
        params.title = title;
        params.description = description;
        params.grade = grade;

        // Convert deadline string to Date
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            params.deadline = formatter.parse(deadline);
        } catch (ParseException e) {
            return ResponseEntity.badRequest().body(new GetResponse<>(400, "Invalid date format"));
        }

        return ResponseEntity.ok(assessmentService.addAssignment(params, id, files));
    }
}
