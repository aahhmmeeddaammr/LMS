package com.LMS.LMS.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Instructor extends User  implements UserDetails {

    public Instructor() {
        role = Role.ROLE_Instructor;
    }

    @OneToMany(mappedBy = "instructor")
    private List<InstructorNotification> notifications = new ArrayList<>();

    @OneToMany(mappedBy = "instructor")
    @JsonIgnore
    private List<Course> courses = new ArrayList<Course>();

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

}
