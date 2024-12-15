package com.LMS.LMS.Services;

import com.LMS.LMS.Controllers.ApiResponses.APIResponse;
import com.LMS.LMS.Controllers.ApiResponses.GetAllResponse;
import com.LMS.LMS.Controllers.ApiResponses.GetResponse;
import com.LMS.LMS.Controllers.ControllerParams.AddCourseParams;
import com.LMS.LMS.Models.Course;
import com.LMS.LMS.Models.Instructor;
import com.LMS.LMS.Repositories.CourseRepository;
import com.LMS.LMS.Repositories.InstructorRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class  CourseService{
    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;
    public CourseService(CourseRepository courseRepository, InstructorRepository instructorRepository){
        this.courseRepository = courseRepository;
        this.instructorRepository = instructorRepository;
    }

    public APIResponse addCourse(AddCourseParams course , int id){
       try{
           Course NewCourse = new Course();
           NewCourse.title = course.Title;
           NewCourse.description = course.Description;
           NewCourse.duration = course.Duration;
           Instructor instructor = instructorRepository.findById(id).orElse(null);
           if(instructor == null){
               throw new IllegalArgumentException("instructor not found");
           }
           NewCourse.instructor=instructor;
           courseRepository.save(NewCourse);
           return new GetResponse<>(200, course);
       }catch(Exception e){
           throw new IllegalArgumentException(e.getMessage());
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
