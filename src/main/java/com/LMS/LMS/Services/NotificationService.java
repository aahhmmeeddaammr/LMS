package com.LMS.LMS.Services;

import com.LMS.LMS.Controllers.ApiResponses.APIResponse;
import com.LMS.LMS.Controllers.ApiResponses.GetResponse;
import com.LMS.LMS.DTOs.NotificationDTO;
import com.LMS.LMS.Models.*;
import com.LMS.LMS.Repositories.*;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class NotificationService {
    private final InstructorRepository instructorRepository;
    private final StudentRepository studentRepository;
    private final StudentNotificationRepository studentNotificationRepository;
    private final InstructorNotificationRepository instructorNotificationRepository;

    public NotificationService(InstructorRepository instructorRepository, StudentRepository studentRepository, StudentNotificationRepository studentNotificationRepository, InstructorNotificationRepository instructorNotificationRepository) {
        this.instructorRepository = instructorRepository;
        this.studentRepository = studentRepository;
        this.studentNotificationRepository = studentNotificationRepository;
        this.instructorNotificationRepository = instructorNotificationRepository;
    }

    public void sendNotificationToStudent(Student student, String message) {
        StudentNotification studentNotification = new StudentNotification();
        studentNotification.setMessage(message);
        studentNotification.setStudent(student);
        studentNotification.setIs_read(false);
        this.studentNotificationRepository.save(studentNotification);
    }

    public void sendNotificationToInstructor(Instructor instructor, String message) {
        InstructorNotification instructorNotification = new InstructorNotification();
        instructorNotification.setMessage(message);
        instructorNotification.setInstructor(instructor);
        instructorNotification.setIs_read(false);
        this.instructorNotificationRepository.save(instructorNotification);
    }

    public APIResponse getUnReadNotifications(int id, String role){
        if(role.equals("ROLE_Student")){
            Student student = studentRepository.findById(id).orElse(null);
            List<NotificationDTO> studentNotifications = studentNotificationRepository.findAllByStudent(student).stream().filter(n -> !n.isIs_read())
                    .sorted(Comparator.comparing(Notification::getID).reversed())
                    .map(n -> {
                        n.setIs_read(true);
                        studentNotificationRepository.save(n);
                        return new NotificationDTO(n);
                    }).toList();
            return new GetResponse<>(200, studentNotifications);
        }
        else if(role.equals("ROLE_Instructor")){
            Instructor instructor = instructorRepository.findById(id).orElse(null);
            List<NotificationDTO> instructorNotifications = instructorNotificationRepository.findAllByInstructor(instructor).stream().filter(n -> !n.isIs_read())
                    .sorted(Comparator.comparing(Notification::getID).reversed())
                    .map(n -> {
                        n.setIs_read(true);
                        instructorNotificationRepository.save(n);
                        return new NotificationDTO(n);
                    }).toList();
            return new GetResponse<>(200, instructorNotifications);
        }
        throw new IllegalArgumentException("You are not allowed to see notifications");
    }

    public APIResponse getAllNotifications(int id, String role){

        if(role.equals("ROLE_Student")){
            Student student = studentRepository.findById(id).orElse(null);
            List<NotificationDTO> studentNotifications = studentNotificationRepository.findAllByStudent(student).stream()
                    .sorted(Comparator.comparing(Notification::getID).reversed())
                    .map(n -> {
                        n.setIs_read(true);
                        studentNotificationRepository.save(n);
                        return new NotificationDTO(n);
                    })
                    .toList();
            return new GetResponse<>(200, studentNotifications);
        }
        else if(role.equals("ROLE_Instructor")){
            Instructor instructor = instructorRepository.findById(id).orElse(null);
            List<NotificationDTO> instructorNotifications = instructorNotificationRepository.findAllByInstructor(instructor).stream()
                    .sorted(Comparator.comparing(Notification::getID).reversed())
                    .map(n -> {
                        n.setIs_read(true);
                        instructorNotificationRepository.save(n);
                        return new NotificationDTO(n);
                    }).toList();
            return new GetResponse<>(200, instructorNotifications);
        }
        throw new IllegalArgumentException("You are not allowed to see notifications");
    }

}
