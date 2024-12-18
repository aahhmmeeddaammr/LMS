package com.LMS.LMS.DTOs;

public class ProfileDTO {
    public String name;
    public String email;
    public String phone;
    public String address;

    public ProfileDTO(String name, String email, String phone, String address) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }
}
