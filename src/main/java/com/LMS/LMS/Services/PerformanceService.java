package com.LMS.LMS.Services;

import com.LMS.LMS.Models.*;
import com.LMS.LMS.Repositories.AttendanceRepository;
import com.LMS.LMS.Repositories.CourseRepository;
import com.LMS.LMS.Repositories.StudentAssignmentRepository;
import com.LMS.LMS.Repositories.StudentQuizzesRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PerformanceService {
    private final StudentQuizzesRepository studentQuizzesRepository;
    private final StudentAssignmentRepository studentAssignmentRepository;
    private final AttendanceRepository attendanceRepository;
    private final CourseRepository courseRepository;

    public PerformanceService(StudentQuizzesRepository studentQuizzesRepository, StudentAssignmentRepository studentAssignmentRepository, AttendanceRepository attendanceRepository, CourseRepository courseRepository) {
        this.studentQuizzesRepository = studentQuizzesRepository;
        this.studentAssignmentRepository = studentAssignmentRepository;
        this.attendanceRepository = attendanceRepository;
        this.courseRepository = courseRepository;
    }

    public List<Attendance> getStudentAttendance(int studentId, int courseId) {
        return attendanceRepository.findAllByStudentId(studentId).stream().filter(a -> a.getLesson().course.getId() == courseId).toList();
    }

    public List<StudentAssignment> getStudentAssignments(int studentId, int courseId) {
        return studentAssignmentRepository.findAllByStudentId(studentId).stream().filter(sa -> sa.getAssignment().getCourse().getId() == courseId).toList();
    }

    public long getTotalClasses(int courseId) {
        return Objects.requireNonNull(courseRepository.findById(courseId).orElse(null)).getLessons().size();
    }

    public long getTotalAssignments(int courseId) {
        return Objects.requireNonNull(courseRepository.findById(courseId).orElse(null)).getAssignments().size();
    }

    public long getTotalQuizzes(int courseId) {
        return Objects.requireNonNull(courseRepository.findById(courseId).orElse(null)).getQuizzes().size();
    }

    public List<StudentsQuizzes> getStudentQuizzes(int studentId, int courseId) {
        return studentQuizzesRepository.findAllByStudentId(studentId).stream().filter(sq -> sq.getQuiz().getCourse().getId() == courseId).toList();
    }

    public List<Student> getStudentsInCourse(int courseId) {
        return Objects.requireNonNull(courseRepository.findById(courseId).orElse(null)).getStudents();
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll().stream().toList();
    }

    public List<Map<String, Object>> generatePerformanceReport() {
        List<Course> courses = getAllCourses();
        List<Map<String, Object>> reportData = new ArrayList<>();
        for (Course course : courses) {
            List<Student> students = getStudentsInCourse(course.getId());

            for (Student student : students) {
                List<Attendance> attendance = getStudentAttendance(student.getId(), course.getId());
                List<StudentAssignment> assignments = getStudentAssignments(student.getId(), course.getId());
                List<StudentsQuizzes> quizzes = getStudentQuizzes(student.getId(), course.getId());

                double sumAssignmentScore = (assignments != null && !assignments.isEmpty())
                        ? assignments.stream()
                        .mapToDouble(assignment -> Optional.of(assignment.getGrade()).orElse(0.0))
                        .sum()
                        : 0.0;

                double sumQuizScore = (quizzes != null && !quizzes.isEmpty())
                        ? quizzes.stream()
                        .mapToDouble(quiz -> Optional.of(quiz.getGrade()).orElse(0.0))
                        .sum()
                        : 0.0;

                long totalClasses = getTotalClasses(course.getId());
                long attendedClasses = attendance.size();
                double attendancePercentage = totalClasses > 0 ? ((double) attendedClasses / totalClasses) * 100 : 0;

                Map<String, Object> reportEntry = new HashMap<>();
                reportEntry.put("studentId", student.getId());
                reportEntry.put("studentName", student.getName() != null ? student.getName() : "Unknown");
                reportEntry.put("courseName", course.getTitle() != null ? course.getTitle() : "Unknown");
                reportEntry.put("averageAssignmentScore", sumAssignmentScore);
                reportEntry.put("averageQuizScore", sumQuizScore);
                reportEntry.put("attendancePercentage", attendancePercentage);
                reportData.add(reportEntry);
            }
        }

        return reportData;
    }

    public List<Map<String, Object>> generateChartReport() {
        List<Course> courses = getAllCourses();
        List<Map<String, Object>> reportData = new ArrayList<>();
        for (Course course : courses) {
            List<Student> students = getStudentsInCourse(course.getId());
            double totalGradesOfAssignment = 0;
            double totalGradesOfQuiz = 0;
            for (Student student : students) {
                List<StudentAssignment> assignments = getStudentAssignments(student.getId(), course.getId());
                List<StudentsQuizzes> quizzes = getStudentQuizzes(student.getId(), course.getId());

                totalGradesOfAssignment += (assignments != null && !assignments.isEmpty())
                        ? assignments.stream()
                        .mapToDouble(assignment -> Optional.of(assignment.getGrade()).orElse(0.0))
                        .sum()
                        : 0.0;

                totalGradesOfQuiz += (quizzes != null && !quizzes.isEmpty())
                        ? quizzes.stream()
                        .mapToDouble(quiz -> Optional.of(quiz.getGrade()).orElse(0.0))
                        .sum()
                        : 0.0;
            }

            Map<String, Object> reportEntry = new HashMap<>();
            reportEntry.put("courseName", course.getTitle() != null ? course.getTitle() : "Unknown");
            reportEntry.put("averageAssignmentScore", totalGradesOfAssignment);
            reportEntry.put("averageQuizScore", totalGradesOfQuiz);
            reportData.add(reportEntry);
        }

        return reportData;
    }
}
