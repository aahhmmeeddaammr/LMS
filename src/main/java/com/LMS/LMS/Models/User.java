package com.LMS.LMS.Models;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.lang.annotation.Inherited;
import java.util.Collection;
import java.util.List;

@MappedSuperclass
public  class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;

    protected String name;

    protected String password;

    @Column(unique = true)
    protected String email;

    @Enumerated(EnumType.STRING)
    protected Role role;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    public  Role getRole(){
        return role;
    }

    public  void setId(int id){
        this.id = id;
    }

    public  void setEmail(String email){
        this.email = email;
    }

    public  void setPassword(String password){
        this.password = password;
    }

    public  void setName(String name){
        this.name = name;
    }

    public  int getId(){
        return id;
    }
    public  String getName(){
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return email;
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return true;
    }

    public  String getEmail(){
        return  this.email;
    }
}
