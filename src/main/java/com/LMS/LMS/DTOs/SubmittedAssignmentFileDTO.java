package com.LMS.LMS.DTOs;

import com.LMS.LMS.Models.StudentAssignment;

import java.util.List;

public class SubmittedAssignmentFileDTO {

    public StudentAssignmentDTO studentData;

    public List<String> files;

    public SubmittedAssignmentFileDTO(List<String> files, StudentAssignment student) {
        this.files = files;
        this.studentData = new StudentAssignmentDTO(student);
    }
}
