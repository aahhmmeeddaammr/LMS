package com.LMS.LMS.Services;

import com.LMS.LMS.Controllers.ApiResponses.APIResponse;
import com.LMS.LMS.Controllers.ControllerParams.AddCourseParams;
import com.LMS.LMS.Controllers.ControllerParams.AddLessonParams;
import com.LMS.LMS.Controllers.ControllerParams.AttendLessonParams;
import com.LMS.LMS.Models.Course;
import com.LMS.LMS.Models.Instructor;
import com.LMS.LMS.Models.Lesson;
import com.LMS.LMS.Models.Student;
import com.LMS.LMS.Repositories.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseServiceTest {
    @InjectMocks
    private CourseService courseService;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private InstructorRepository instructorRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private LessonRepository lessonRepository;
    @Mock
    private AttendanceRepository attendanceRepository;
    @Mock
    private ModelFileRepository modelFileRepository;
    @Mock
    private UploadFileService uploadFileService;
    @Mock
    private EmailService emailService;
    @Mock
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addCourse() {
        AddCourseParams courseParams = new AddCourseParams();
        courseParams.Title = "Advanced Software";
        courseParams.Description = "A course on software.";
        courseParams.Duration = 30;
        Instructor instructor = new Instructor();
        instructor.setId(1);
        when(instructorRepository.findById(instructor.getId())).thenReturn(Optional.of(instructor));
        APIResponse response = courseService.addCourse(courseParams, instructor.getId());

        assertNotNull(response);
        assertEquals(200, response.status);
        verify(courseRepository, times(1)).save(any(Course.class));
        verify(instructorRepository, times(1)).findById(instructor.getId());
    }

    @Test
    void getAllCourses() {
        Instructor instructor = new Instructor();
        instructor.setId(1);
        Course course1 = new Course();
        course1.setTitle("Introduction to Software");
        course1.setLessons(new ArrayList<>());
        course1.setInstructor(instructor);
        course1.setStudents(new ArrayList<>());
        course1.setFiles(new ArrayList<>());
        Course course2 = new Course();
        course2.setTitle("Advanced Software");
        course2.setLessons(new ArrayList<>());
        course2.setInstructor(instructor);
        course2.setStudents(new ArrayList<>());
        course2.setFiles(new ArrayList<>());
        when(courseRepository.findAll()).thenReturn(List.of(course1, course2));
        APIResponse response = courseService.getAllCourses("Instructor");
        assertNotNull(response);
        assertEquals(200, response.status);
        verify(courseRepository, times(1)).findAll();
    }


    @Test
    void getCourseById() {
        Instructor instructor = new Instructor();
        instructor.setId(1);
        Course course = new Course();
        course.setId(1);
        course.setTitle("Advanced Software");
        course.setInstructor(instructor);
        course.setStudents(new ArrayList<>());
        course.setFiles(new ArrayList<>());
        course.setLessons(new ArrayList<>());
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));
        APIResponse response = courseService.getCourseById(1, "Instructor");

        assertNotNull(response);
        assertEquals(200, response.status);
        verify(courseRepository, times(1)).findById(course.getId());
    }


    @Test
    void enrollStudent() {
        Course course = new Course();
        course.setId(1);
        course.setTitle("Advanced Software");
        course.setInstructor(new Instructor());
        course.setStudents(new ArrayList<>());
        course.setFiles(new ArrayList<>());
        Student student = new Student();
        student.setId(1);
        student.setCourses(new ArrayList<>());
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        APIResponse response = courseService.enrollStudent(course.getId(), student.getId());

        assertNotNull(response);
        assertEquals(200, response.status);
        verify(courseRepository, times(1)).findById(1);
        verify(studentRepository, times(1)).findById(1);
    }


    @Test
    void deleteStudent() {
        Course course = new Course();
        course.setId(1);
        course.setTitle("Advanced Software");
        course.setInstructor(new Instructor());
        course.setStudents(new ArrayList<>());
        course.setFiles(new ArrayList<>());
        Student student = new Student();
        student.setId(1);
        student.setCourses(new ArrayList<>());
        student.getCourses().add(course);
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        APIResponse response = courseService.deleteStudent(course.getId(), student.getId());

        assertNotNull(response);
        assertEquals(200, response.status);
        verify(courseRepository, times(1)).findById(course.getId());
        verify(studentRepository, times(1)).findById(student.getId());
    }
    @Test
    void addLesson() throws Exception {
        AddLessonParams params = new AddLessonParams();
        params.CourseId = 1;
        params.title = "Lesson 1";
        params.content = "Lesson content";
        Course course = new Course();
        course.setId(1);
        course.setTitle("Advanced Software");
        course.setInstructor(new Instructor());
        course.setStudents(new ArrayList<>());
        course.setLessons(new ArrayList<>());
        Lesson lesson = new Lesson();
        lesson.id=1;
        lesson.title="Lesson 1";
        lesson.content="Lesson content";
        lesson.OTP="12345";
        lesson.course=course;
        when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));
        when(courseRepository.save(course)).thenReturn(course);
        when(lessonRepository.save(lesson)).thenReturn(lesson);
        APIResponse response = courseService.addLesson(params);

        assertNotNull(response);
        assertEquals(200, response.status);
        verify(courseRepository, times(1)).findById(course.getId());
        verify(lessonRepository, times(1)).save(any(Lesson.class));
    }




    @Test
    void attendLesson() throws Exception {
        AttendLessonParams params = new AttendLessonParams();
        params.OTP ="12345";
        Lesson lesson = new Lesson();
        lesson.OTP = "12345";
        lesson.title= "Lesson 1";
        Student student = new Student();
        student.setId(1);
        when(lessonRepository.findByOTP(lesson.OTP)).thenReturn(lesson);
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        APIResponse response = courseService.attendLesson(params, student.getId());

        assertNotNull(response);
        assertEquals(200, response.status);
        verify(lessonRepository, times(1)).findByOTP(lesson.OTP);
        verify(studentRepository, times(1)).findById(student.getId());
    }


    @Test
    void addMaterial() {
        MultipartFile file = new MockMultipartFile("file", "test.png", "image/png", "".getBytes());
        Course course = new Course();
        course.setId(1);
        course.setTitle("Advanced Software");
        course.setInstructor(new Instructor());
        course.setStudents(new ArrayList<>());
        course.setFiles(new ArrayList<>());
        when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));
        when(uploadFileService.uploadFile(file)).thenReturn("file/path/test.png");
        APIResponse response = courseService.addMaterial(file, course.getId());

        assertNotNull(response);
        assertEquals(200, response.status);
        verify(courseRepository, times(1)).findById(course.getId());
        verify(uploadFileService, times(1)).uploadFile(file);
    }

}