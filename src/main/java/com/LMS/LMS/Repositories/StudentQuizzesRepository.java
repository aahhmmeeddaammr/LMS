package com.LMS.LMS.Repositories;

import com.LMS.LMS.Models.StudentsQuizzes;
import com.LMS.LMS.Models.StudentsQuizzesPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentQuizzesRepository extends JpaRepository<StudentsQuizzes, StudentsQuizzesPK> {
}
