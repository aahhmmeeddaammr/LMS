package com.LMS.LMS.Repositories;

import com.LMS.LMS.Models.Attendance;
import com.LMS.LMS.Models.AttendancePK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<Attendance, AttendancePK> {
}
