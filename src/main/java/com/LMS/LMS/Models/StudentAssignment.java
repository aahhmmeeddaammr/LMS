package com.LMS.LMS.Models;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@IdClass(StudentAssignmentPK.class)
public class StudentAssignment {

    @Id
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Id
    @ManyToOne
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;

    @Column
    private double Grade;

    @Column
    private String Feedback;

    @Column
    private boolean isCorrected;

    @OneToMany(mappedBy = "submittedAssignment")
    private List<SubmittedAssignmentFile> files = new ArrayList<>();

    public List<SubmittedAssignmentFile> getFiles() {
        return files;
    }

    public void setFiles(List<SubmittedAssignmentFile> files) {
        this.files = files;
    }

    public String getFeedback() {
        return Feedback;
    }

    public void setFeedback(String feedback) {
        Feedback = feedback;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    public double getGrade() {
        return Grade;
    }

    public void setGrade(double grade) {
        Grade = grade;
    }

    public boolean getIsCorrected() {
        return isCorrected;
    }

    public void setIsCorrected(boolean corrected) {
        isCorrected = corrected;
    }
}
