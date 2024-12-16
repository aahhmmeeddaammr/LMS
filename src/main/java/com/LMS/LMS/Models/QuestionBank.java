package com.LMS.LMS.Models;


import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "question_bank")
public class QuestionBank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToMany(mappedBy = "questionBank", cascade = CascadeType.ALL)
    private List<Question> questions;
}
