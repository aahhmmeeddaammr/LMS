package com.LMS.LMS.Repositories;

import com.LMS.LMS.Models.StudentAssignment;
import com.LMS.LMS.Models.StudentAssignmentPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentAssignmentRepository extends JpaRepository<StudentAssignment, StudentAssignmentPK> {

    List<StudentAssignment> findAllByAssignmentId(int assignment_id);

}
