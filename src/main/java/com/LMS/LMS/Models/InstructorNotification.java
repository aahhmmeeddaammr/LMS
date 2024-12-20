package com.LMS.LMS.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class InstructorNotification extends Notification {

    @ManyToOne
    Instructor instructor = new Instructor();

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }
}
