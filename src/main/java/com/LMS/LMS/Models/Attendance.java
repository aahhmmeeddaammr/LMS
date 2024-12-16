package com.LMS.LMS.Models;

import jakarta.persistence.*;

@Entity
@IdClass(AttendancePK.class)
public class Attendance {

    @Id
    @ManyToOne
    @JoinColumn(name = "studentId", referencedColumnName = "id")
    private Student student;

    @Id
    @ManyToOne
    @JoinColumn(name = "lessonId", referencedColumnName = "id")
    private Lesson lesson;

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }
}
