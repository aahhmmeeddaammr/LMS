package com.LMS.LMS.DTOs;

import com.LMS.LMS.Models.Quiz;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class QuizDTO {

    public QuizDTO(Quiz quiz) {
        this.title = quiz.getTitle();
        this.score = quiz.getScore();
        this.duration = quiz.getDuration();
        this.startDate = quiz.getStartDate();
        this.course = quiz.course.getTitle();
        this.questions = quiz.getQuestions().stream().map(QuestionDTO::new).toList();
    }

    public String title;

    public double score;

    public double duration;

    public LocalDateTime startDate;

    public List<QuestionDTO> questions;

    public String course;
}
