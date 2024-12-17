package com.LMS.LMS.Models;

import jakarta.persistence.Entity;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
public class Admin extends User implements UserDetails {
    public Admin() {
        this.role = Role.ROLE_ADMIN;
    }
}
