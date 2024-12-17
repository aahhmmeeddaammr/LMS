package com.LMS.LMS.Repositories;

import com.LMS.LMS.Models.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
}
