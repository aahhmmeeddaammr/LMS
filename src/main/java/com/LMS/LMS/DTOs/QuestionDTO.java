package com.LMS.LMS.DTOs;

import com.LMS.LMS.Models.Question;
import com.LMS.LMS.Models.QuestionType;

import java.util.List;

public class QuestionDTO {

    public QuestionDTO(Question question) {
        switch (question.getQuestionType()) {
            case QuestionType.MCQ -> this.questionType = "MCQ";
            case QuestionType.TrueOrFalse -> this.questionType = "True or False";
            case QuestionType.ShortAnswer -> this.questionType = "Short Answer";
        }
        this.questionTitle = question.getQuestionTitle();
        this.answers = question.getAnswers().stream().map(AnswerDTO::new).toList();
    }

    public String questionType;

    public String questionTitle;

    public List<AnswerDTO> answers;
}
