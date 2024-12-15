package com.LMS.LMS.Repositories;

import com.LMS.LMS.Models.Instructor;
import com.LMS.LMS.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InstructorRepository extends JpaRepository<Instructor, Integer> {
    Optional<User> findByEmail(String userEmail);
}
