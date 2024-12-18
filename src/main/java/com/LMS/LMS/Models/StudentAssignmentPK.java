package com.LMS.LMS.Models;

import java.io.Serializable;
import java.util.Objects;

public class StudentAssignmentPK implements Serializable {

    private Student student;
    private Assignment assignment;

    public StudentAssignmentPK(Student student, Assignment assignment) {
        this.student = student;
        this.assignment = assignment;
    }

    public StudentAssignmentPK() {}

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentAssignmentPK that = (StudentAssignmentPK) o;
        return student.equals(that.student) && assignment.equals(that.assignment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(student, assignment);
    }

}
