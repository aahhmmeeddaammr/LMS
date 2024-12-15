package com.LMS.LMS.Models;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    @Column
    public String title;
    @Column
    public double grade;
    @Column
    public Date deadline;
    @ManyToOne
    public Course course;
    @OneToMany(mappedBy = "assignment")
    List<AssignmentFile> files;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<AssignmentFile> getFiles() {
        return files;
    }

    public void setFiles(List<AssignmentFile> files) {
        this.files = files;
    }
}
