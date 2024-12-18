package com.LMS.LMS.Services;

import com.LMS.LMS.Controllers.ApiResponses.APIResponse;
import com.LMS.LMS.Controllers.ApiResponses.GetResponse;
import com.LMS.LMS.Controllers.ControllerParams.AddAssignmentParams;
import com.LMS.LMS.Controllers.ControllerParams.AddQuestionsParams;
import com.LMS.LMS.Controllers.ControllerParams.AddQuizParams;
import com.LMS.LMS.Controllers.ControllerParams.SubmitQuizParams;
import com.LMS.LMS.DTOs.CourseDTO;
import com.LMS.LMS.DTOs.QuizDTO;
import com.LMS.LMS.Models.*;
import com.LMS.LMS.Repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AssessmentService {
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final QuizRepository quizRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final StudentQuizzesRepository studentQuizzesRepository;
    private final UploadFileService uploadFileService;
    private final AssignmentFileRepository assignmentFileRepository;
    private final AssignmentRepository assignmentRepository;
    private final  EmailService emailService;
    public AssessmentService(QuestionRepository questionRepository, AnswerRepository answerRepository, QuizRepository quizRepository, CourseRepository courseRepository, StudentRepository studentRepository, StudentQuizzesRepository studentQuizzesRepository, UploadFileService uploadFileService, AssignmentFileRepository assignmentFileRepository, AssignmentRepository assignmentRepository, EmailService emailService) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.quizRepository = quizRepository;
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
        this.studentQuizzesRepository = studentQuizzesRepository;
        this.uploadFileService = uploadFileService;
        this.assignmentFileRepository = assignmentFileRepository;
        this.assignmentRepository = assignmentRepository;
        this.emailService = emailService;
    }

    public APIResponse createQuestionBank(List<AddQuestionsParams> paramsList, int CrsId) {
        Course course = courseRepository.findById(CrsId).orElse(null);
        if (course == null) {
            throw new IllegalArgumentException("Course not found");
        }

        List<Question> newQuestions = new ArrayList<>();

        for (AddQuestionsParams params : paramsList) {
            Question question = new Question();
            question.setQuestionTitle(params.title);
            switch (params.type) {
                case "MCQ" -> question.setQuestionType(QuestionType.MCQ);
                case "TrueOrFalse" -> question.setQuestionType(QuestionType.TrueOrFalse);
                case "ShortAnswer" -> question.setQuestionType(QuestionType.ShortAnswer);
            }
            question.setCourse(course);

            List<Answer> answers = new ArrayList<>();

            for (Answer answer : params.answers) {
                answer.setQuestion(question);
                answers.add(answer);
            }

            question.setAnswers(answers);

            questionRepository.save(question);

            answerRepository.saveAll(answers);

            newQuestions.add(question);
        }

        course.setQuestionBank(newQuestions);

        courseRepository.save(course);

        return new GetResponse<>(200, "Questions Added Successfully");
    }

    public APIResponse createQuiz(AddQuizParams params, int CrsId) {
        Course course = courseRepository.findById(CrsId).orElse(null);
        if (course == null) {
            throw new IllegalArgumentException("Course not found");
        }
        if (params.noOfQuestions > course.getQuestionBank().size()) {
            throw new IllegalArgumentException("Number of questions is greater than the number of questions in question bank");
        }
        List<Student>students= course.getStudents();
        for (Student student : students) {
            Email email = new Email();
            email.setRecipient(student.getEmail());
            email.setSubject("Student Deletion Successfully");
            email.setMsgBody("Student Deletion Successfully");
            emailService.sendSimpleMail(email);
        }
        Quiz quiz = new Quiz();
        quiz.setTitle(params.title);
        quiz.setDuration(params.duration);
        quiz.setStartDate(params.startDate);
        quiz.setScore(params.score);
        quiz.setCourse(course);
        List<Question> newQuestions = GenerateQuiz(params.noOfQuestions, course.getQuestionBank());
        quiz.setQuestions(newQuestions);
        quizRepository.save(quiz);
        return new GetResponse<>(200, "Quiz Created Successfully");
    }

    public APIResponse getQuiz(int id) {
        Quiz quiz = quizRepository.findById(id).orElse(null);
        if (quiz == null) {
            throw new IllegalArgumentException("Quiz not found");
        }
        QuizDTO quizDTO = new QuizDTO(quiz);
        return new GetResponse<>(200, quizDTO);
    }

    public APIResponse submitQuiz(SubmitQuizParams params, int id, int stdId) {
        Quiz quiz = quizRepository.findById(id).orElse(null);
        if (quiz == null) {
            throw new IllegalArgumentException("Quiz not found");
        }
        Student student = studentRepository.findById(stdId).orElse(null);
        if (student == null) {
            throw new IllegalArgumentException("Student not found");
        }
        if (studentQuizzesRepository.findById(new StudentsQuizzesPK(student, quiz)).orElse(null) != null) {
            throw new IllegalArgumentException("You have submitted the quiz already");
        }
        double grade = 0;
        List<Question> questions = quiz.getQuestions();
        double weight = quiz.getScore() / questions.size();
        for (int i = 0; i < questions.size(); i++) {
            Answer correctAnswer = questions.get(i).getAnswers().stream().filter(Answer::getIsCorrect).toList().getFirst();
            if (Objects.equals(correctAnswer.getAnswer(), params.answers.get(i))) {
                grade += weight;
            }
        }
        StudentsQuizzes studentsQuizzes = new StudentsQuizzes();
        studentsQuizzes.setQuiz(quiz);
        studentsQuizzes.setStudent(student);
        studentsQuizzes.setGrade(grade);
        studentQuizzesRepository.save(studentsQuizzes);
        return new GetResponse<>(200, "Quiz Submitted Successfully with grade :  " + grade);
    }

    public APIResponse addAssignment(AddAssignmentParams params, int crsId, List<MultipartFile> files) {
        Course course = courseRepository.findById(crsId).orElse(null);
        if (course == null) {
            throw new IllegalArgumentException("Course not found");
        }
        Assignment assignment = new Assignment();
        assignment.setCourse(course);
        assignment.setTitle(params.title);
        assignment.setDescription(params.description);
        assignment.setGrade(params.grade);
        assignment.setDeadline(params.deadline);

        assignmentRepository.save(assignment);

        for (MultipartFile file : files) {
            String NewFilePathName = uploadFileService.uploadFile(file);
            AssignmentFile NewFile = new AssignmentFile();
            NewFile.setAssignment(assignment);
            NewFile.FilePath = NewFilePathName;
            assignment.getFiles().add(NewFile);
            assignmentFileRepository.save(NewFile);
        }
        courseRepository.save(course);
        return new GetResponse<>(200, "Assignment Added Successfully");
    }

    private List<Question> GenerateQuiz(int noOfQuestions, List<Question> questions) {
        List<Question> quizQuestions = new ArrayList<>();
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < noOfQuestions; i++) {
            int x = (int) Math.floor(Math.random() * questions.size());
            if (numbers.isEmpty()) {
                quizQuestions.add(questions.get(x));
                numbers.add(x);
                continue;
            }
            do {
                x = (int) Math.floor(Math.random() * questions.size());
            } while (numbers.contains(x));
            numbers.add(x);
            quizQuestions.add(questions.get(x));
        }
        return quizQuestions;
    }
}