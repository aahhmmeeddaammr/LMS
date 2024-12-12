package com.LMS.LMS.Models;
import jakarta.persistence.*;
@Entity
@Table
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int id;
    @Column
    public String name;
    public Course(){
    }
    public Course(int id, String name){
        this.id = id;
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
