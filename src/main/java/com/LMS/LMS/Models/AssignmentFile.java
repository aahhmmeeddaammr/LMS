package com.LMS.LMS.Models;

import jakarta.persistence.*;

@Entity
public class AssignmentFile extends File {

    @ManyToOne
    private Assignment assignment;

    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }
}
