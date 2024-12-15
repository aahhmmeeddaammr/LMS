package com.LMS.LMS.Models;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @Column
    public String title;

    @Column
    public String content;

    @Column
    public Date date = new Date();

    @Column
    public String OTP;

    @ManyToOne
    public Course course;
}
