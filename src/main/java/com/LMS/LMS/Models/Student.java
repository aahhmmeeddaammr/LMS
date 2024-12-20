package com.LMS.LMS.Models;

import jakarta.persistence.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Student extends User implements UserDetails {

    @OneToMany(mappedBy = "student")
    List<StudentNotification> notifications = new ArrayList<>();

    @ManyToMany
    List<Course> courses = new ArrayList<Course>();

    public Student() {
        role = Role.ROLE_Student;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public List<StudentNotification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<StudentNotification> notifications) {
        this.notifications = notifications;
    }
}
