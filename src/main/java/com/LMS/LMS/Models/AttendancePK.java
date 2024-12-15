package com.LMS.LMS.Models;

import java.io.Serializable;
import java.util.Objects;

public class AttendancePK implements Serializable {

    private Student student;  // Use the entity class
    private Lesson lesson;    // Use the entity class

    // Constructors
    public AttendancePK() {}

    public AttendancePK(Student student, Lesson lesson) {
        this.student = student;
        this.lesson = lesson;
    }

    // Getters and Setters
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttendancePK that = (AttendancePK) o;
        return student.equals(that.student) && lesson.equals(that.lesson);
    }

    @Override
    public int hashCode() {
        return Objects.hash(student, lesson);
    }
}
