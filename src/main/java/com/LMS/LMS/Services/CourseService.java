package com.LMS.LMS.Services;

import com.LMS.LMS.Controllers.ApiResponses.APIResponse;
import com.LMS.LMS.Controllers.ApiResponses.GetAllResponse;
import com.LMS.LMS.Controllers.ApiResponses.GetResponse;
import com.LMS.LMS.Controllers.ControllerParams.AddCourseParams;
import com.LMS.LMS.Controllers.ControllerParams.AddLessonParams;
import com.LMS.LMS.Controllers.ControllerParams.AttendLessonParams;
import com.LMS.LMS.DTOs.CourseDTO;
import com.LMS.LMS.Models.*;
import com.LMS.LMS.Repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.stream.Collectors;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;
    private final StudentRepository studentRepository;
    private final LessonRepository lessonRepository;
    private final AttendanceRepository attendanceRepository;
    private final ModelFileRepository modelFileRepository;
    private final UploadFileService uploadFileService;
    private final EmailService emailService;
    public CourseService(CourseRepository courseRepository, InstructorRepository instructorRepository, StudentRepository studentRepository, LessonRepository lessonRepository, AttendanceRepository attendanceRepository, ModelFileRepository modelFileRepository, UploadFileService uploadFileService, EmailService emailService) {
        this.courseRepository = courseRepository;
        this.instructorRepository = instructorRepository;
        this.studentRepository = studentRepository;
        this.lessonRepository = lessonRepository;
        this.attendanceRepository = attendanceRepository;
        this.modelFileRepository = modelFileRepository;
        this.uploadFileService = uploadFileService;
        this.emailService = emailService;
    }

    public APIResponse addCourse(AddCourseParams course, int id) {
        try {
            Course NewCourse = new Course();
            NewCourse.setTitle(course.Title);
            NewCourse.setDescription(course.Description);
            NewCourse.setDuration(course.Duration);
            Instructor instructor = instructorRepository.findById(id).orElse(null);
            if (instructor == null) {
                throw new IllegalArgumentException("Instructor not found");
            }
            NewCourse.setInstructor(instructor);
            courseRepository.save(NewCourse);
            return new GetResponse<>(200, course);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public APIResponse getAllCourses(String role) {
        boolean includeStudents = !role.equals("ROLE_Student");
        return new GetAllResponse<>(200, courseRepository.findAll().stream().map(course -> new CourseDTO(course, includeStudents)).collect(Collectors.toList()));
    }

    public APIResponse getCourseById(int id, String role) {
        var course = courseRepository.findById(id).orElse(null);
        if (course == null) {
            throw new IllegalArgumentException("Course not found");
        }
        boolean includeStudents = !role.equals("ROLE_Student");
        var courseDTO = new CourseDTO(course, includeStudents);
        return new GetResponse<>(200, courseDTO);
    }

    public APIResponse enrollStudent(int CrsId, int StdId) {
        var course = courseRepository.findById(CrsId).orElse(null);
        var student = studentRepository.findById(StdId).orElse(null);
        if (course == null || student == null) {
            throw new IllegalArgumentException("Course or student not found");
        }
        course.getStudents().add(student);
        student.getCourses().add(course);
        studentRepository.save(student);
        courseRepository.save(course);
        return new GetResponse<>(200, "Student Enrolled Successfully");
    }

    public APIResponse deleteStudent(int CrsId, int StdId) {
        var course = courseRepository.findById(CrsId).orElse(null);
        if (course == null) {
            throw new IllegalArgumentException("Course not found");
        }
        var student = studentRepository.findById(StdId).orElse(null);
        if (student == null) {
            throw new IllegalArgumentException("Student not found");
        }
        Email email = new Email();
        email.setRecipient(student.getEmail());
        email.setSubject("Student Deletion Successfully");
        email.setMsgBody("Student Deletion Successfully");
        course.getStudents().remove(student);
        student.getCourses().remove(course);
        emailService.sendSimpleMail(email);
        studentRepository.save(student);
        courseRepository.save(course);
        return new GetResponse<>(200, "Student Deleted Successfully");
    }

    public APIResponse addLesson(AddLessonParams params) throws Exception {
        var course = courseRepository.findById(params.CourseId).orElse(null);
        if (course == null) {
            throw new IllegalArgumentException("Course not found");
        }
        try {
            String otp = GenerateOTP();
            var lesson = new Lesson();
            lesson.title = params.title;
            lesson.content = params.content;
            lesson.OTP = otp;
            lesson.course = course;
            course.getLessons().add(lesson);
            courseRepository.save(course);
            lessonRepository.save(lesson);
            return new GetResponse<>(200, "Lesson Added Successfully");
        } catch (Exception e) {
            throw new Exception("Internal Server Error");
        }
    }

    public APIResponse attendLesson(AttendLessonParams params, int StdId) throws Exception {
        Lesson lesson = lessonRepository.findByOTP(params.OTP);
        if (lesson == null) {
            throw new IllegalArgumentException("Lesson not found or you have entered a wrong OTP");
        }
        try {
            Student student = studentRepository.findById(StdId).orElse(null);
            Attendance attendance = new Attendance();
            attendance.setStudent(student);
            attendance.setLesson(lesson);
            attendanceRepository.save(attendance);
            return new GetResponse<>(200, "Lesson Attended Successfully");
        } catch (Exception e) {
            throw new Exception("Internal Server Error");
        }
    }

    public APIResponse addMaterial(MultipartFile file, int Course_id) {
        String NewFilePathName = uploadFileService.uploadFile(file);
        MediaFile NewFile = new MediaFile();
        Course course = courseRepository.findById(Course_id).orElse(null);
        if (course == null) {
            throw new IllegalArgumentException("Course not found");
        }
        NewFile.setCourse(course);
        NewFile.FilePath = NewFilePathName;
        course.getFiles().add(NewFile);
        courseRepository.save(course);
        modelFileRepository.save(NewFile);
        return new GetResponse<>(200, new CourseDTO(course, false));
    }

    private String GenerateOTP() {
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < 10; ++i) {
            otp.append((int) Math.floor(Math.random() * 10));
        }
        if (lessonRepository.findByOTP(otp.toString()) == null) {
            return GenerateOTP();
        }
        return otp.toString();
    }
}
