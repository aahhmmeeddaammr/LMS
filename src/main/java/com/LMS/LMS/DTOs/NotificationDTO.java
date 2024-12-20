package com.LMS.LMS.DTOs;

import com.LMS.LMS.Models.InstructorNotification;
import com.LMS.LMS.Models.StudentNotification;

public class NotificationDTO {

    public String message;
    public boolean isRead;

    public NotificationDTO(StudentNotification studentNotification) {
        this.message = studentNotification.getMessage();
        this.isRead = studentNotification.isIs_read();
    }

    public NotificationDTO(InstructorNotification instructorNotification) {
        this.message = instructorNotification.getMessage();
        this.isRead =  instructorNotification.isIs_read();
    }
}
