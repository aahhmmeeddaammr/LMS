package com.LMS.LMS.DTOs;

import com.LMS.LMS.Models.Course;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseDTO {
    public int id;

    public String title;

    public String description;

    public int duration;

    public InstructorDTO instructor;

    public List<LessonDTO> lessons;

    public List<StudentDTO> students;

    public CourseDTO(Course course, boolean includeStudents) {
        this.id = course.getId();
        this.title = course.getTitle();
        this.description = course.getDescription();
        this.duration = course.getDuration();
        this.lessons = course.getLessons().stream().map(LessonDTO::new).collect(Collectors.toList());
        this.instructor = new InstructorDTO(course.getInstructor());
        this.students = includeStudents ? course.getStudents().stream().map(StudentDTO::new).collect(Collectors.toList()) : null;
    }
}
