package com.LMS.LMS.DTOs;

import com.LMS.LMS.Models.StudentAssignment;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Optional;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentAssignmentDTO {

    public StudentDTO student;

    public boolean isCorrected;

    public Optional<Double> grade;

    public String feedback;

    public StudentAssignmentDTO(StudentAssignment student) {
        this.student = new StudentDTO(student.getStudent());
        this.isCorrected = student.getIsCorrected();
        this.grade = this.isCorrected ? Optional.of(student.getGrade()) : null;
        this.feedback = this.isCorrected ? student.getFeedback() : null;
    }
}
