package com.LMS.LMS.Repositories;

import com.LMS.LMS.Models.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    Lesson findByOTP(String OTP);
}
