package com.LMS.LMS.Services;


import com.LMS.LMS.Controllers.ApiResponses.APIResponse;
import com.LMS.LMS.Controllers.ControllerParams.AddAssignmentParams;
import com.LMS.LMS.Controllers.ControllerParams.AddQuestionsParams;
import com.LMS.LMS.Controllers.ControllerParams.AddQuizParams;
import com.LMS.LMS.Controllers.ControllerParams.SubmitQuizParams;
import com.LMS.LMS.Models.*;
import com.LMS.LMS.Repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AssessmentServiceTest {

    @InjectMocks
    private AssessmentService assessmentService;
    @Mock
    private QuestionRepository questionRepository;
    @Mock
    private AnswerRepository answerRepository;
    @Mock
    private QuizRepository quizRepository;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private StudentQuizzesRepository studentQuizzesRepository;
    @Mock
    private AssignmentFileRepository assignmentFileRepository;
    @Mock
    private  AssignmentRepository assignmentRepository;
    @Mock
    private EmailService emailService;
    @Mock
    private  StudentAssignmentRepository studentAssignmentRepository;
    @Mock
    private UploadFileService uploadFileService;
    @Mock
    private SubmittedAssignmentFileRepository submittedAssignmentFileRepository;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createQuestionBank() {
        Course course = new Course();
        course.setId(1);
        course.setTitle("Advanced Software");
        List<Student> students = new ArrayList<>();
        Student student = new Student();
        student.setEmail("enas@mail.com");
        students.add(student);
        course.setStudents(students);
        when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));

        List<AddQuestionsParams> paramsList = new ArrayList<>();
        AddQuestionsParams questionParam = new AddQuestionsParams();
        questionParam.title = "MCQ Questions";
        questionParam.type = "MCQ";
        questionParam.answers = new ArrayList<>();
        paramsList.add(questionParam);

        APIResponse response = assessmentService.createQuestionBank(paramsList, course.getId());

        assertNotNull(response);
        assertEquals(200, response.status);
        verify(courseRepository, times(1)).findById(course.getId());
        verify(questionRepository, times(1)).save(any(Question.class));
        verify(answerRepository, times(1)).saveAll(questionParam.answers);
        verify(courseRepository, times(1)).save(course);
        verify(emailService, times(1)).sendSimpleMail(any(Email.class));
    }


    @Test
    void createQuiz() {
        Course course = new Course();
        course.setId(1);
        course.setTitle("Advanced Software");
        List<Student> students = new ArrayList<>();
        Student student = new Student();
        student.setEmail("enas@mail.com");
        students.add(student);
        course.setStudents(students);
        when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));

        List<Question> questionBank = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            Question question = new Question();
            question.setId(i);
            question.setQuestionTitle("Question " + i);
            questionBank.add(question);
        }
        course.setQuestionBank(questionBank);

        when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));
        when(quizRepository.save(any(Quiz.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AddQuizParams params = new AddQuizParams();
        params.title = "Lab Quiz";
        params.duration = 30;
        params.startDate = LocalDateTime.of(2024,12,20,8,30);
        params.score = 10;
        params.noOfQuestions = 10;
        APIResponse response = assessmentService.createQuiz(params, course.getId());

        assertNotNull(response);
        assertEquals(200, response.status);
        verify(courseRepository, times(1)).findById(course.getId());
        verify(quizRepository, times(1)).save(any(Quiz.class));
        verify(emailService, times(1)).sendSimpleMail(any(Email.class));
    }

    @Test
    void getQuiz() {
        Course course = new Course();
        course.setId(1);
        course.setTitle("Advanced Software");

        List<Question> questions = new ArrayList<>();
        List<Answer>answers=new ArrayList<>();
        Question question = new Question();
        question.setId(1);
        question.setQuestionTitle("Question 1");
        question.setQuestionType(QuestionType.valueOf("MCQ"));
        question.setAnswers(answers);
        questions.add(question);

        Quiz quiz = new Quiz();
        quiz.setId(1);
        quiz.setTitle("quiz 1");
        quiz.setDuration(30);
        quiz.setScore(10);
        quiz.setStartDate(LocalDateTime.of(2024, 12, 20, 8, 30));
        quiz.setQuestions(questions);
        quiz.course=course;

        when(quizRepository.findById(quiz.getId())).thenReturn(Optional.of(quiz));
        APIResponse response = assessmentService.getQuiz(quiz.getId());

        assertNotNull(response);
        assertEquals(200, response.status);
        verify(quizRepository, times(1)).findById(1);
    }


    @Test
    void submitQuiz() {
        Quiz quiz = new Quiz();
        quiz.setId(1);
        quiz.setTitle("Quiz 2");
        quiz.setDuration(30);
        quiz.setScore(10);
        quiz.setStartDate(LocalDateTime.now().minusMinutes(10));

        List<Question> questions = new ArrayList<>();
        Question question = new Question();
        question.setId(1);
        question.setQuestionTitle("Question 1");
        Answer answer = new Answer();
        answer.setIsCorrect(true);
        question.setAnswers(List.of(answer));
        questions.add(question);
        quiz.setQuestions(questions);

        Student student = new Student();
        student.setId(1);
        student.setEmail("enas@mail.com");

        SubmitQuizParams params = new SubmitQuizParams();
        params.answers = List.of("");

        when(quizRepository.findById(quiz.getId())).thenReturn(Optional.of(quiz));
        when(studentRepository.findById(student.getId())).thenReturn(Optional.of(student));
        when(studentQuizzesRepository.findById(any())).thenReturn(Optional.empty());
        APIResponse response = assessmentService.submitQuiz(params, quiz.getId(), student.getId());

        assertNotNull(response);
        assertEquals(200, response.status);
        verify(studentQuizzesRepository, times(1)).save(any(StudentsQuizzes.class));
        verify(emailService, times(1)).sendSimpleMail(any(Email.class));
    }


    @Test
    void addAssignment() {
        Course course = new Course();
        course.setId(1);
        course.setTitle("Advanced");
        when(courseRepository.findById(course.getId())).thenReturn(java.util.Optional.of(course));

        MultipartFile file = new MockMultipartFile("file", "test.png", "text/plain", "".getBytes());
        when(uploadFileService.uploadFile(file)).thenReturn("file/path/test.png");
        AddAssignmentParams params = new AddAssignmentParams();
        params.title = "Assignment 1";
        params.description = "Description for assignment 1";
        params.grade = 10;

        List<Student> students = new ArrayList<>();
        Student student = new Student();
        student.setEmail("enas@mail.com");
        students.add(student);
        course.setStudents(students);
        APIResponse response = assessmentService.addAssignment(params, course.getId(), List.of(file));

        assertNotNull(response);
        assertEquals(200, response.status);
        verify(courseRepository, times(1)).findById(course.getId());
        verify(assignmentRepository, times(1)).save(any(Assignment.class));
        verify(assignmentFileRepository, times(1)).save(any(AssignmentFile.class));
        verify(courseRepository, times(1)).save(course);
        verify(emailService, times(1)).sendSimpleMail(any(Email.class));
        verify(uploadFileService, times(1)).uploadFile(file);
    }
    @Test
    void submitAssignment() {
        Student student = new Student();
        student.setId(1);
        student.setEmail("enas@mail.com");
        Assignment assignment = new Assignment();
        assignment.setId(1);
        assignment.setTitle("Assignment 1");
        when(studentRepository.findById(student.getId())).thenReturn(java.util.Optional.of(student));
        when(assignmentRepository.findById(assignment.getId())).thenReturn(java.util.Optional.of(assignment));
        StudentAssignmentPK pk = new StudentAssignmentPK(student, assignment);
        when(studentAssignmentRepository.findById(pk)).thenReturn(java.util.Optional.empty());
        MultipartFile file = new MockMultipartFile("file", "test.png", "image/png", "".getBytes());
        when(uploadFileService.uploadFile(file)).thenReturn("file/path/test.png");
        List<MultipartFile> files = List.of(file);
        APIResponse response = assessmentService.submitAssignment(files, assignment.getId(), student.getId());

        assertNotNull(response);
        assertEquals(200, response.status);
        verify(studentRepository, times(1)).findById(student.getId());
        verify(assignmentRepository, times(1)).findById(assignment.getId());
        verify(studentAssignmentRepository, times(1)).findById(pk);
        verify(studentAssignmentRepository, times(1)).save(any(StudentAssignment.class));
        verify(uploadFileService, times(1)).uploadFile(file);
        verify(submittedAssignmentFileRepository, times(1)).save(any(SubmittedAssignmentFile.class));
    }
}

