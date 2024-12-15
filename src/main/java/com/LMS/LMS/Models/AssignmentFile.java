package com.LMS.LMS.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class AssignmentFile  extends File{
    @ManyToOne
    public Assignment assignment;

}
