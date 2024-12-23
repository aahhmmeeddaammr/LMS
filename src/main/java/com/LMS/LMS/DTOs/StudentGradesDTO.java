package com.LMS.LMS.DTOs;

import com.LMS.LMS.Models.StudentAssignment;
import com.LMS.LMS.Models.StudentsQuizzes;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentGradesDTO {
    public String type;
    public double grade;
    public StudentQuizDTO studentQuiz;
    public String AssignmentId;
    public String AssignmentName;
    public StudentGradesDTO(StudentAssignment assignment) {
        this.type="Assignment";
        this.grade=assignment.getGrade();
        this.AssignmentId = String.valueOf(assignment.getAssignment().getId());
        this.AssignmentName = assignment.getAssignment().getTitle();
        this.studentQuiz = null;
    }
    public StudentGradesDTO(StudentsQuizzes quiz) {
        this.type = "Quiz";
        this.grade=quiz.getGrade();
        this.studentQuiz = new StudentQuizDTO(quiz);
        this.AssignmentId =null;
        this.AssignmentName = null;
    }
}
