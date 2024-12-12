package com.LMS.LMS.Repositories;
import com.LMS.LMS.Models.Course;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CourseRepository extends JpaRepository<Course, Integer> {

}