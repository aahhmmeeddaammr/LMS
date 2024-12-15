package com.LMS.LMS.Repositories;
import com.LMS.LMS.Models.Admin;
import com.LMS.LMS.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Optional<User> findByEmail(String userEmail);
}
