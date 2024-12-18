package com.LMS.LMS.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class SubmittedAssignmentFile extends File {

    @ManyToOne
    private StudentAssignment submittedAssignment;

    public StudentAssignment getSubmittedAssignment() {
        return submittedAssignment;
    }

    public void setSubmittedAssignment(StudentAssignment submittedAssignment) {
        this.submittedAssignment = submittedAssignment;
    }
}
