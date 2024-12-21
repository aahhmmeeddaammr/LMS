package com.LMS.LMS.Services;

import com.LMS.LMS.Models.Email;
import com.LMS.LMS.Models.Quiz;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}") private String sender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEnrollmentEmail(String courseName, Email email) {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("courseName", courseName);
        placeholders.put("userName", email.getRecipient().split("@")[0]);
        sendSimpleMail(email, placeholders, "/templates/EnrollEmail.html");
    }

    public void sendAddMaterialEmail(String courseName, Email email) {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("courseName", courseName);
        placeholders.put("studentName", email.getRecipient().split("@")[0]);
        placeholders.put("materialLink", email.getAttachment());
        sendSimpleMail(email, placeholders, "/Templates/AddMatrialEmail.html");
    }

    public void sendAddLessonEmail(String courseName, Email email, String lesson) {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("courseName", courseName);
        placeholders.put("studentName", email.getRecipient().split("@")[0]);
        placeholders.put("lessonTitle", lesson);
        sendSimpleMail(email, placeholders, "/Templates/AddLessonEmail.html");
    }

    public void sendRemoveFromCourseEmail(String courseName, Email email) {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("courseName", courseName);
        placeholders.put("studentName", email.getRecipient().split("@")[0]);
        sendSimpleMail(email, placeholders, "/Templates/DeleteStudentFromCourseEmail.html");
    }

    public void sendAddQuizEmail(String courseName, Quiz quiz, Email email) {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("courseName", courseName);
        placeholders.put("studentName", email.getRecipient().split("@")[0]);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'at' hh:mm a");
        String formattedStartDate = quiz.getStartDate().format(formatter);

        placeholders.put("startDate", formattedStartDate + " with duration of " + quiz.getDuration() + " hours");
        placeholders.put("quizTitle", quiz.getTitle());

        sendSimpleMail(email, placeholders, "/Templates/AddQuizEmail.html");
    }

    public void sendAssignmentFeedbackAndGrade(String assignmentName, Email email, Double studentGrade , String feedback ){
         Map<String, String> placeholders = new HashMap<>();
         placeholders.put("assignmentTitle", assignmentName);
         placeholders.put("studentGrade", studentGrade.toString());
         placeholders.put("feedback", feedback);
         placeholders.put("grade", studentGrade.toString());
         sendSimpleMail(email, placeholders, "/Templates/AssignmentGradeEmail.html");
    }

    public void sendGradeEmail(String courseName, Quiz quiz, Email email , Double StudentGrade) {
         Map<String, String> placeholders = new HashMap<>();
         placeholders.put("courseName", courseName);
         placeholders.put("studentName", email.getRecipient().split("@")[0]);
         placeholders.put("quizTitle", quiz.getTitle());
         placeholders.put("grade", StudentGrade.toString());
         sendSimpleMail(email, placeholders, "/Templates/QuizGradeEmail.html");
    }

    private void sendSimpleMail(Email email, Map<String, String> placeholders, String mailHTML) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(email.getRecipient());
            System.out.println(0);
            String template = readEmailTemplate(mailHTML);
            if (template == null) {
                throw new IllegalArgumentException("Email template not found.");
            }
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                template = template.replace("{{" + entry.getKey() + "}}", entry.getValue());
            }
            System.out.println(1);

            mimeMessageHelper.setText(template, true); // true enables HTML content

            if (email.getAttachment() != null) {
                handleAttachment(email.getAttachment(), mimeMessageHelper);
            }

            mimeMessageHelper.setSubject(email.getSubject());

            javaMailSender.send(mimeMessage);

        } catch (MessagingException | IOException e) {
            e.printStackTrace();
            e.getMessage();
        }
    }

    private String readEmailTemplate(String templatePath) throws IOException {
        try (InputStream inputStream = EmailService.class.getResourceAsStream(templatePath)) {
            if (inputStream == null) {
                return null; // Template not found
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    private void handleAttachment(String attachmentUrl, MimeMessageHelper mimeMessageHelper) throws IOException, MessagingException {
        URL fileUrl = new URL(attachmentUrl);
        String fileName = Paths.get(fileUrl.getPath()).getFileName().toString();

        Path tempFile = Files.createTempFile(null, fileName);
        try (InputStream in = fileUrl.openStream()) {
            Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
        }

        mimeMessageHelper.addAttachment(fileName, tempFile.toFile());
        tempFile.toFile().deleteOnExit();
    }
}
