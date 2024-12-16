package com.LMS.LMS.Models;

import java.io.Serializable;
import java.util.Objects;

public class StudentsQuizzesPK implements Serializable {
    private Student student;
    private Quiz quiz;

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public StudentsQuizzesPK() {}

    public StudentsQuizzesPK(Student student, Quiz quiz) {
        this.student = student;
        this.quiz = quiz;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentsQuizzesPK that = (StudentsQuizzesPK) o;
        return student.equals(that.getStudent()) && quiz.equals(that.getQuiz());
    }

    @Override
    public int hashCode() {
        return Objects.hash(student, quiz);
    }
}
