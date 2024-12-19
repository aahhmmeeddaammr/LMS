package com.LMS.LMS.DTOs;


import com.LMS.LMS.Models.User;

public class ProfileDTO {
    public String name;
    public String email;
    public String phone;
    public String address;

    public ProfileDTO(User user) {
        this.name = user.getUsername();
        this.email = user.getUsername();
        this.phone = user.getPhone();
        this.address = user.getAddress();
    }
}
