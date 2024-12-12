package com.LMS.LMS.Services;

import com.LMS.LMS.Controllers.ApiResponses.APIResponse;
import com.LMS.LMS.Controllers.ApiResponses.GetAllResponse;
import com.LMS.LMS.Controllers.ApiResponses.GetResponse;
import com.LMS.LMS.Models.Course;
import com.LMS.LMS.Repositories.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class  CourseService{
    private CourseRepository courseRepository;

    public CourseService( CourseRepository courseRepository){
        this.courseRepository = courseRepository;
    }

    public APIResponse addCourse(Course course){
        try {
            return new GetResponse<>(200 , courseRepository.save(course)) ;
        }catch (Exception e){
            return new GetResponse<>(400 , "Error In Adding Course ") ;
        }
    }

    public APIResponse getAllCourses(){
        return new GetAllResponse<>(200 , courseRepository.findAll()) ;
    }

    public APIResponse getCourseById(int id){
        var course = courseRepository.findById(id).orElse(null);
        if (course == null){
            return new GetResponse<>(404 , "Error In Getting Course ") ;
        }
        return new GetResponse<>(200 , course) ;

    }
}
