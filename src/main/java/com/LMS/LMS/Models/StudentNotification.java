package com.LMS.LMS.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class StudentNotification extends Notification {

    @ManyToOne
    Student student = new Student();

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
