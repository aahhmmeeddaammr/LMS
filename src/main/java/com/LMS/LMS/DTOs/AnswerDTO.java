package com.LMS.LMS.DTOs;

import com.LMS.LMS.Models.Answer;

public class AnswerDTO {

    public AnswerDTO(Answer answer) {
        this.answer = answer.getAnswer();
    }

    private String answer;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

}
