package com.LMS.LMS.Repositories;

import com.LMS.LMS.Models.User;
import org.apache.catalina.UserDatabase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String userEmail);

}
