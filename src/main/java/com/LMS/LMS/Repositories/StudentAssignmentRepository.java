package com.LMS.LMS.Repositories;

import com.LMS.LMS.Models.StudentAssignment;
import com.LMS.LMS.Models.StudentAssignmentPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentAssignmentRepository extends JpaRepository<StudentAssignment, StudentAssignmentPK> {
}
