package com.LMS.LMS.Repositories;

import com.LMS.LMS.Models.Student;
import com.LMS.LMS.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    Optional<User> findByEmail(String userEmail);
}
