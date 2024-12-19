package com.LMS.LMS.Repositories;

import com.LMS.LMS.Models.StudentAssignment;
import com.LMS.LMS.Models.SubmittedAssignmentFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubmittedAssignmentFileRepository extends JpaRepository<SubmittedAssignmentFile, Integer> {

    List<SubmittedAssignmentFile> findAllBySubmittedAssignment(StudentAssignment studentAssignment);
}
