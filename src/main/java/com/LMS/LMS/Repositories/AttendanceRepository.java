package com.LMS.LMS.Repositories;

import com.LMS.LMS.Models.Attendance;
import com.LMS.LMS.Models.AttendancePK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, AttendancePK> {

    List<Attendance> findAllByStudentId(int id);
}
