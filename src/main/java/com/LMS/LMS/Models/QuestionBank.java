package com.LMS.LMS.Models;


import jakarta.persistence.*;

import java.util.List;

@Entity
public class QuestionBank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @OneToOne(mappedBy = "questionBank")
    public Course course;

    @OneToMany(mappedBy = "questionBank")
    public List<Question> questions;
}
