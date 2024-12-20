package com.LMS.LMS.Models;

import jakarta.persistence.*;

@MappedSuperclass
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;

    @Column
    private String message;

    @Column
    private boolean is_read;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isIs_read() {
        return is_read;
    }

    public void setIs_read(boolean is_read) {
        this.is_read = is_read;
    }
}