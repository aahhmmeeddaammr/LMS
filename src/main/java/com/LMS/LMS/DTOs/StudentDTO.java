package com.LMS.LMS.DTOs;

import com.LMS.LMS.Models.Student;

public class StudentDTO {
    public int id;
    public String name;
    public String email;

    StudentDTO(Student student) {
        this.id = student.getId();
        this.name = student.getName();
        this.email = student.getEmail();
    }
}
