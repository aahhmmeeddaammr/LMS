package com.LMS.LMS.Services;

import com.LMS.LMS.Controllers.ApiResponses.APIResponse;
import com.LMS.LMS.Controllers.ApiResponses.GetResponse;
import com.LMS.LMS.Controllers.ControllerParams.*;
import com.LMS.LMS.DTOs.QuizDTO;
import com.LMS.LMS.DTOs.StudentAssignmentDTO;
import com.LMS.LMS.DTOs.SubmittedAssignmentFileDTO;
import com.LMS.LMS.Models.*;
import com.LMS.LMS.Repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
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
    private final EmailService emailService;
    private final StudentAssignmentRepository studentAssignmentRepository;
    private final SubmittedAssignmentFileRepository submittedAssignmentFileRepository;
    private final NotificationService notificationService;

    public AssessmentService(QuestionRepository questionRepository, AnswerRepository answerRepository, QuizRepository quizRepository, CourseRepository courseRepository, StudentRepository studentRepository, StudentQuizzesRepository studentQuizzesRepository, UploadFileService uploadFileService, AssignmentFileRepository assignmentFileRepository, AssignmentRepository assignmentRepository, EmailService emailService, StudentAssignmentRepository studentAssignmentRepository, SubmittedAssignmentFileRepository studentSubmittedAssignmentFileRepository, NotificationService notificationService) {
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
        this.studentAssignmentRepository = studentAssignmentRepository;
        this.submittedAssignmentFileRepository = studentSubmittedAssignmentFileRepository;
        this.notificationService = notificationService;
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

        List<Student> students = course.getStudents();
        for (Student student : students) {
            Email email = new Email();
            email.setRecipient(student.getEmail());
            email.setSubject("New Questions Added");
            email.setMsgBody("New questions have been added to the course: " + course.getTitle());
            emailService.sendSimpleMail(email);
        }

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
        List<Student> students = course.getStudents();
        for (Student student : students) {
            Email email = new Email();
            email.setRecipient(student.getEmail());
            email.setSubject("New Quiz Created");
            email.setMsgBody("A new quiz titled '" + params.title + "' has been created in the course: " + course.getTitle());
            emailService.sendSimpleMail(email);
            notificationService.sendNotificationToStudent(student, "A new quiz titled '" + params.title + "' has been created in the '" + course.getTitle() + "' course with duration '" + params.duration + " hours'.");
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
        LocalDateTime currentTime = LocalDateTime.now();

        if (currentTime.isBefore(quiz.getStartDate())) {
            return new GetResponse<>(400, "Quiz has not started yet. Please wait until the start date.");
        }
        Student student = studentRepository.findById(stdId).orElse(null);
        if (student == null) {
            throw new IllegalArgumentException("Student not found");
        }
        if (studentQuizzesRepository.findById(new StudentsQuizzesPK(student, quiz)).orElse(null) != null) {
            throw new IllegalArgumentException("You have submitted the quiz already");
        }
        double grade = 0;
        LocalDateTime quizEndTime = quiz.getStartDate().plusMinutes((long) quiz.getDuration());
        if (currentTime.isAfter(quizEndTime)) {
            StudentsQuizzes studentsQuizzes = new StudentsQuizzes();
            studentsQuizzes.setQuiz(quiz);
            studentsQuizzes.setStudent(student);
            studentsQuizzes.setGrade(grade);
            studentQuizzesRepository.save(studentsQuizzes);

            Email email = new Email();
            email.setRecipient(student.getEmail());
            email.setSubject("Quiz Submission");
            email.setMsgBody("You submitted the quiz late. Your grade is 0.");
            emailService.sendSimpleMail(email);
            notificationService.sendNotificationToStudent(student, "You submitted the quiz late :( .");

            return new GetResponse<>(400, "Quiz has ended. Submission not allowed. Your Grade Is 0");

        }
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

        Email email = new Email();
        email.setRecipient(student.getEmail());
        email.setSubject("Quiz Submission Confirmation");
        email.setMsgBody("You submitted the quiz '" + quiz.getTitle() + "' with a grade: " + grade);
        emailService.sendSimpleMail(email);
        notificationService.sendNotificationToStudent(student, "You submitted the quiz titled '" + quiz.getTitle() + "' successfully, your grade is: " + grade);

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


        List<Student> students = course.getStudents();
        for (Student student : students) {
            Email email = new Email();
            email.setRecipient(student.getEmail());
            email.setSubject("New Assignment Added");
            email.setMsgBody("A new assignment titled '" + params.title + "' has been added to the course: " + course.getTitle());
            emailService.sendSimpleMail(email);
            notificationService.sendNotificationToStudent(student, "A new assignment titled '" + params.title + "' has been added to the '" + course.getTitle() + "' course.");
        }

        return new GetResponse<>(200, "Assignment Added Successfully");
    }

    public APIResponse submitAssignment(List<MultipartFile> files, int assignmentId, int studentId) {
        Student student = studentRepository.findById(studentId).orElse(null);
        if (student == null) {
            throw new IllegalArgumentException("Student not found");
        }
        Assignment assignment = assignmentRepository.findById(assignmentId).orElse(null);
        if (assignment == null) {
            throw new IllegalArgumentException("Assignment not found");
        }
        StudentAssignmentPK pk = new StudentAssignmentPK(student, assignment);
        StudentAssignment studentAssignment = studentAssignmentRepository.findById(pk).orElse(null);
        if (studentAssignment != null) {
            throw new IllegalArgumentException("You have already submitted the assignment");
        }
        studentAssignment = new StudentAssignment();
        studentAssignment.setStudent(student);
        studentAssignment.setAssignment(assignment);
        studentAssignmentRepository.save(studentAssignment);
        for (MultipartFile file : files) {
            String NewFilePathName = uploadFileService.uploadFile(file);
            SubmittedAssignmentFile NewFile = new SubmittedAssignmentFile();
            NewFile.setSubmittedAssignment(studentAssignment);
            NewFile.FilePath = NewFilePathName;
            studentAssignment.getFiles().add(NewFile);
            submittedAssignmentFileRepository.save(NewFile);
        }
        notificationService.sendNotificationToStudent(student, "Assignment titled '" + assignment.getTitle() + "' Submitted Successfully.");
        return new GetResponse<>(200, "Assignment Submitted Successfully");
    }

    public APIResponse getAllStudentAssignments(int assignmentId) {
        List<StudentAssignment> studentAssignments = studentAssignmentRepository.findAllByAssignmentId(assignmentId);
        if (studentAssignments.isEmpty()) {
            throw new IllegalArgumentException("No Assignments Found");
        }
        List<StudentAssignmentDTO> studentAssignmentDTOS = studentAssignments.stream().map(StudentAssignmentDTO::new).toList();
        return new GetResponse<>(200, studentAssignmentDTOS);
    }

    public APIResponse getStudentAssignment(GetStudentAssignmentParams param) {
        Assignment assignment = assignmentRepository.findById(param.assignmentId).orElse(null);
        if (assignment == null) {
            throw new IllegalArgumentException("Assignment not found");
        }
        Student student = studentRepository.findById(param.studentId).orElse(null);
        if (student == null) {
            throw new IllegalArgumentException("Student not found");
        }
        StudentAssignmentPK pk = new StudentAssignmentPK(student, assignment);
        StudentAssignment studentAssignment = studentAssignmentRepository.findById(pk).orElse(null);
        if (studentAssignment == null) {
            throw new IllegalArgumentException("Student haven't submitted the assignment");
        }
        SubmittedAssignmentFileDTO dto =
                new SubmittedAssignmentFileDTO(
                        submittedAssignmentFileRepository.findAllBySubmittedAssignment(studentAssignment).stream().map(File::getFilePath).toList()
                        , studentAssignment);
        return new GetResponse<>(200, dto);
    }

    public APIResponse correctAssignment(CorrectAssignmentParams param) {
        Assignment assignment = assignmentRepository.findById(param.assignmentId).orElse(null);
        if (assignment == null) {
            throw new IllegalArgumentException("Assignment not found");
        }
        Student student = studentRepository.findById(param.studentId).orElse(null);
        if (student == null) {
            throw new IllegalArgumentException("Student not found");
        }
        StudentAssignmentPK pk = new StudentAssignmentPK(student, assignment);
        StudentAssignment studentAssignment = studentAssignmentRepository.findById(pk).orElse(null);
        if (studentAssignment == null) {
            throw new IllegalArgumentException("Student haven't submitted the assignment");
        }
        if (studentAssignment.getIsCorrected()) {
            throw new IllegalArgumentException("Student assignment has already been corrected");
        }
        studentAssignment.setIsCorrected(true);
        studentAssignment.setGrade(param.grade);
        studentAssignment.setFeedback(param.feedback);
        studentAssignmentRepository.save(studentAssignment);
        notificationService.sendNotificationToStudent(student, "Assignment titled '" + assignment.getTitle() + "' is corrected successfully, your grade is " + param.grade);

        return new GetResponse<>(200, "Assignment Corrected Successfully");
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