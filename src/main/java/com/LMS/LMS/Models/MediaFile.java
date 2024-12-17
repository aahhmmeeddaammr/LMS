package com.LMS.LMS.Models;

import jakarta.persistence.*;

@Entity
public class MediaFile extends File {
    @ManyToOne
    private Course course;

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
