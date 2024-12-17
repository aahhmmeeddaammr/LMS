package com.LMS.LMS.Models;

import jakarta.persistence.*;

@Entity
@IdClass(StudentsQuizzesPK.class)
public class StudentsQuizzes {
    @Id
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Id
    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @Column(nullable = false)
    private double grade;

    // Getters and setters
    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }
}
