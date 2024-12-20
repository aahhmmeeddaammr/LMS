package com.LMS.LMS.Repositories;

import com.LMS.LMS.Models.Student;
import com.LMS.LMS.Models.StudentNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentNotificationRepository extends JpaRepository<StudentNotification, Integer> {

    List<StudentNotification> findAllByStudent(Student student);
}
