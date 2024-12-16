package com.LMS.LMS.Models;

import jakarta.persistence.*;

@Entity
@IdClass(StudentsQuizzesPK.class)
public class StudentsQuizzes {
    @Id
    @ManyToOne
    @JoinColumn(name = "studentId", referencedColumnName = "id")
    private Student student;
    @Id
    @ManyToOne
    @JoinColumn(name = "quizId", referencedColumnName = "id")
    private Quiz quiz;

    private double grade;
}
