package com.LMS.LMS.Models;

import jakarta.persistence.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Student extends User  implements UserDetails {


    @ManyToMany
    List<Course> courses = new ArrayList<Course>();

    public Student() {
        role = Role.ROLE_Student;
    }
    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

}
