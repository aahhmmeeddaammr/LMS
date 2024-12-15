package com.LMS.LMS.Models;

import jakarta.persistence.*;

@Entity
public class MediaFile extends File {
    @ManyToOne
    public Course course;
}
