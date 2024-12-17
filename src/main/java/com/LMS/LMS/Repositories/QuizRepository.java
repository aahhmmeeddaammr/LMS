package com.LMS.LMS.Repositories;

import com.LMS.LMS.Models.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Integer> {
}
