package com.LMS.LMS.Controllers;

import com.LMS.LMS.Controllers.ApiResponses.APIResponse;
import com.LMS.LMS.Controllers.ApiResponses.GetResponse;
import com.LMS.LMS.Controllers.ControllerParams.*;
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
            @PathVariable int id
    ) {

        AddAssignmentParams params = new AddAssignmentParams();
        params.title = title;
        params.description = description;
        params.grade = grade;

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            params.deadline = formatter.parse(deadline);
        } catch (ParseException e) {
            return ResponseEntity.badRequest().body(new GetResponse<>(400, "Invalid date format"));
        }

        return ResponseEntity.ok(assessmentService.addAssignment(params, id, files));
    }

    @PostMapping("/submit-assignment/{id}")
    public ResponseEntity<APIResponse> submitAssignment(@RequestParam("files") List<MultipartFile> files, @RequestHeader String Authorization, @PathVariable int id) {
        String token = Authorization.substring(7);
        var Claims = jwtService.ExtractClaimsFromJWT(token);
        int stdID = Claims.get("id", Integer.class);
        return ResponseEntity.ok(assessmentService.submitAssignment(files, id, stdID));
    }

    @GetMapping("/get-students-assignments/{id}")
    public ResponseEntity<APIResponse> getStudentsAssignments(@PathVariable int id) {
        return ResponseEntity.ok(assessmentService.getAllStudentAssignments(id));
    }

    @GetMapping("/get-student-assignment")
    public ResponseEntity<APIResponse> getStudentAssignment(@RequestBody GetStudentAssignmentParams params) {
        return ResponseEntity.ok(assessmentService.getStudentAssignment(params));
    }

    @PostMapping("/correct-assignment")
    public ResponseEntity<APIResponse> correctAssignment(@RequestBody CorrectAssignmentParams params) {
        return ResponseEntity.ok(assessmentService.correctAssignment(params));
    }
    @GetMapping("/get-all-quiz-grades/{id}")
    public ResponseEntity<APIResponse> getAllQuizGrades(@PathVariable int id){
        return ResponseEntity.ok(assessmentService.getAllStudentsMarksInQuiz(id));
    }
    @GetMapping("/get-all-quizzes-for-student/{id}")
    public ResponseEntity<APIResponse> getAllQuizzesForStudent(@PathVariable int id){
        return ResponseEntity.ok(assessmentService.getAllQuizMarksForStudent(id));
    }
    @GetMapping("/get-all-grades/{cid}/{sid}")
    public ResponseEntity<APIResponse> getAllGrades(@PathVariable int cid, @PathVariable int sid) {
        return ResponseEntity.ok(assessmentService.getAllStudentMarksInCourse(sid,cid));
    }
}
