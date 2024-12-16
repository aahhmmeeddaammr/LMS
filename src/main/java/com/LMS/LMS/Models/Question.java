package com.LMS.LMS.Models;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String question;

    @OneToOne
    private Answer correct_answer;

    @OneToMany
    private List<Answer> wrong_answer;

    @Enumerated(EnumType.STRING)
    private QuestionType question_type;

    @ManyToOne
    private Quiz quiz;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Answer getCorrect_answer() {
        return correct_answer;
    }

    public void setCorrect_answer(Answer correct_answer) {
        this.correct_answer = correct_answer;
    }

    public List<Answer> getWrong_answer() {
        return wrong_answer;
    }

    public void setWrong_answer(List<Answer> wrong_answer) {
        this.wrong_answer = wrong_answer;
    }

    public QuestionType getQuestion_type() {
        return question_type;
    }

    public void setQuestion_type(QuestionType question_type) {
        this.question_type = question_type;
    }
}
