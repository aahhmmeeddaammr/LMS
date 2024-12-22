package com.LMS.LMS.DTOs;

import com.LMS.LMS.Models.StudentsQuizzes;

public class StudentQuizDTO {
    public int quizId;
    public String quizName;
    public String studentName;
    public int studentId;
    public double quizScore;
    public StudentQuizDTO(StudentsQuizzes studentsQuizzes) {
        this.quizId=studentsQuizzes.getQuiz().getId();
        this.quizName=studentsQuizzes.getQuiz().getTitle();
        this.quizScore=studentsQuizzes.getGrade();
        this.studentName=studentsQuizzes.getStudent().getName();
        this.studentId=studentsQuizzes.getStudent().getId();
    }
}
