package com.LMS.LMS.Repositories;

import com.LMS.LMS.Models.Instructor;
import com.LMS.LMS.Models.InstructorNotification;
import com.LMS.LMS.Models.Student;
import com.LMS.LMS.Models.StudentNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InstructorNotificationRepository extends JpaRepository<InstructorNotification, Integer> {

    List<InstructorNotification> findAllByInstructor(Instructor instructor);
}
