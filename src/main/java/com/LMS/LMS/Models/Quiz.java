package com.LMS.LMS.Models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String title;

    @Column
    private double Score;

    @Column
    private double Duration;

    @Column
    private LocalDateTime StartDate;

    @ManyToMany
    public List<Question> questions;

    @ManyToOne
    public Course course;

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

    public double getScore() {
        return Score;
    }

    public void setScore(double score) {
        Score = score;
    }

    public double getDuration() {
        return Duration;
    }

    public void setDuration(double duration) {
        Duration = duration;
    }

    public LocalDateTime getStartDate() {
        return StartDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        StartDate = startDate;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
