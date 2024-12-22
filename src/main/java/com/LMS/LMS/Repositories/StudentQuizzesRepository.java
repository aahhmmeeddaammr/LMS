package com.LMS.LMS.Repositories;

import com.LMS.LMS.Models.StudentsQuizzes;
import com.LMS.LMS.Models.StudentsQuizzesPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentQuizzesRepository extends JpaRepository<StudentsQuizzes, StudentsQuizzesPK> {

    List<StudentsQuizzes> findAllByStudentId(int studentId);
    List<StudentsQuizzes> findAllByQuizId(int quizId);
}
