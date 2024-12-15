package com.LMS.LMS.DTOs;

import com.LMS.LMS.Models.Instructor;

public class InstructorDTO {
    public int id;
    public String name;
    public String email;

    InstructorDTO(Instructor instructor) {
        this.id = instructor.getId();
        this.name = instructor.getName();
        this.email = instructor.getEmail();
    }
}
