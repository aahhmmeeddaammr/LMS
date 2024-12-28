package com.LMS.LMS.Services;

import com.LMS.LMS.Models.*;
import com.LMS.LMS.Repositories.AttendanceRepository;
import com.LMS.LMS.Repositories.CourseRepository;
import com.LMS.LMS.Repositories.StudentAssignmentRepository;
import com.LMS.LMS.Repositories.StudentQuizzesRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PerformanceService {
    private final StudentQuizzesRepository studentQuizzesRepository;
    private final StudentAssignmentRepository studentAssignmentRepository;
    private final AttendanceRepository attendanceRepository;
    private final CourseRepository courseRepository;

    public PerformanceService(StudentQuizzesRepository studentQuizzesRepository,
                              StudentAssignmentRepository studentAssignmentRepository,
                              AttendanceRepository attendanceRepository,
                              CourseRepository courseRepository) {
        this.studentQuizzesRepository = studentQuizzesRepository;
        this.studentAssignmentRepository = studentAssignmentRepository;
        this.attendanceRepository = attendanceRepository;
        this.courseRepository = courseRepository;
    }

    public List<Attendance> getStudentAttendance(int studentId, int courseId) {
        return attendanceRepository.findAllByStudentId(studentId).stream()
                .filter(a -> a.getLesson().course.getId() == courseId)
                .toList();
    }

    public List<StudentAssignment> getStudentAssignments(int studentId, int courseId) {
        return studentAssignmentRepository.findAllByStudentId(studentId).stream()
                .filter(sa -> sa.getAssignment().getCourse().getId() == courseId)
                .toList();
    }

    public long getTotalClasses(int courseId) {
        Course course = courseRepository.findById(courseId).orElse(null);
        return course != null ? course.getLessons().size() : 0;
    }

    public long getTotalAssignments(int courseId) {
        Course course = courseRepository.findById(courseId).orElse(null);
        return course != null ? course.getAssignments().size() : 0;
    }

    public long getTotalQuizzes(int courseId) {
        Course course = courseRepository.findById(courseId).orElse(null);
        return course != null ? course.getQuizzes().size() : 0;
    }

    public List<StudentsQuizzes> getStudentQuizzes(int studentId, int courseId) {
        return studentQuizzesRepository.findAllByStudentId(studentId).stream()
                .filter(sq -> sq.getQuiz().getCourse().getId() == courseId)
                .toList();
    }

    public List<Student> getStudentsInCourse(int courseId) {
        Course course = courseRepository.findById(courseId).orElse(null);
        return course != null ? course.getStudents() : Collections.emptyList();
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Map<String, Object> generateCoursePerformanceReport(int courseId) {
        Course course = courseRepository.findById(courseId).orElse(null);
        if (course == null) {
            throw new IllegalArgumentException("Course not found");
        }

        Map<String, Object> reportData = new HashMap<>();
        reportData.put("courseName", course.getTitle());
        reportData.put("courseId", course.getId());

        List<Student> students = getStudentsInCourse(courseId);

        List<Map<String, Object>> studentDataList = new ArrayList<>();

        // Get assignments and quizzes with their max grades
        List<Assignment> assignments = course.getAssignments();
        List<Quiz> quizzes = course.getQuizzes();

        List<Map<String, Object>> assignmentsInfo = assignments.stream()
                .map(assignment -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", assignment.getId());
                    map.put("maxMarks", assignment.getGrade());
                    return map;
                }).collect(Collectors.toList());
        reportData.put("assignmentsInfo", assignmentsInfo);

        List<Map<String, Object>> quizzesInfo = quizzes.stream()
                .map(quiz -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", quiz.getId());
                    map.put("maxMarks", quiz.getScore());
                    return map;
                }).collect(Collectors.toList());
        reportData.put("quizzesInfo", quizzesInfo);

        for (Student student : students) {
            Map<String, Object> studentData = new HashMap<>();
            studentData.put("studentId", student.getId());
            studentData.put("studentName", student.getName());

            Map<Integer, Double> assignmentGrades = new HashMap<>();

            for (Assignment assignment : assignments) {
                StudentAssignment studentAssignment = studentAssignmentRepository.findByStudentIdAndAssignmentId(student.getId(), assignment.getId());
                Double grade = studentAssignment != null ? studentAssignment.getGrade() : null;
                assignmentGrades.put(assignment.getId(), grade);
            }
            studentData.put("assignmentGrades", assignmentGrades);

            Map<Integer, Double> quizGrades = new HashMap<>();

            for (Quiz quiz : quizzes) {
                StudentsQuizzes studentQuiz = studentQuizzesRepository.findByStudentIdAndQuizId(student.getId(), quiz.getId());
                Double grade = studentQuiz != null ? studentQuiz.getGrade() : null;
                quizGrades.put(quiz.getId(), grade);
            }
            studentData.put("quizGrades", quizGrades);

            List<Attendance> attendanceRecords = getStudentAttendance(student.getId(), course.getId());
            long totalClasses = course.getLessons().size();
            long attendedClasses = attendanceRecords.size();
            double attendancePercentage = totalClasses > 0 ? ((double) attendedClasses / totalClasses) * 100 : 0;
            studentData.put("attendancePercentage", attendancePercentage);

            double totalAssignmentScore = assignmentGrades.values().stream()
                    .filter(Objects::nonNull)
                    .mapToDouble(Double::doubleValue)
                    .sum();
            double totalQuizScore = quizGrades.values().stream()
                    .filter(Objects::nonNull)
                    .mapToDouble(Double::doubleValue)
                    .sum();
            double totalScore = totalAssignmentScore + totalQuizScore;

            studentData.put("totalAssignmentScore", totalAssignmentScore);
            studentData.put("totalQuizScore", totalQuizScore);
            studentData.put("totalScore", totalScore);

            studentDataList.add(studentData);
        }

        reportData.put("students", studentDataList);

        return reportData;
    }

    public List<Map<String, Object>> generateAllCoursesPerformanceReport() {
        List<Course> courses = getAllCourses();
        List<Map<String, Object>> allCoursesReportData = new ArrayList<>();
        for (Course course : courses) {
            Map<String, Object> courseReport = generateCoursePerformanceReport(course.getId());
            allCoursesReportData.add(courseReport);
        }
        return allCoursesReportData;
    }

    public List<Map<String, Object>> generateAttendanceReport() {
        List<Course> courses = getAllCourses();
        List<Map<String, Object>> reportData = new ArrayList<>();
        for (Course course : courses) {
            Map<String, Object> courseData = generateAttendanceReport(course.getId());
            reportData.add(courseData);
        }
        return reportData;
    }

    public Map<String, Object> generateAttendanceReport(int courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course ID"));
        Map<String, Object> reportData = new HashMap<>();
        reportData.put("courseName", course.getTitle() != null ? course.getTitle() : "Unknown");

        List<Student> students = getStudentsInCourse(courseId);
        double totalAttendancePercentage = 0.0;

        for (Student student : students) {
            List<Attendance> attendanceRecords = getStudentAttendance(student.getId(), courseId);
            long totalClasses = course.getLessons().size();
            long attendedClasses = attendanceRecords.size();
            double attendancePercentage = totalClasses > 0 ? ((double) attendedClasses / totalClasses) * 100 : 0;
            totalAttendancePercentage += attendancePercentage;
        }

        double averageAttendancePercentage = students.size() > 0 ? (totalAttendancePercentage / students.size()) : 0;
        reportData.put("averageAttendancePercentage", averageAttendancePercentage);

        return reportData;
    }

    public List<Map<String, Object>> generateAssignmentReport() {
        List<Course> courses = getAllCourses();
        List<Map<String, Object>> reportData = new ArrayList<>();
        for (Course course : courses) {
            Map<String, Object> courseData = generateAssignmentReport(course.getId());
            reportData.add(courseData);
        }
        return reportData;
    }

    public Map<String, Object> generateAssignmentReport(int courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course ID"));

        Map<String, Object> reportData = new HashMap<>();
        reportData.put("courseName", course.getTitle() != null ? course.getTitle() : "Unknown");

        List<Student> students = getStudentsInCourse(courseId);
        double totalAssignmentScores = 0.0;
        int totalAssignments = 0;

        for (Student student : students) {
            List<StudentAssignment> assignments = getStudentAssignments(student.getId(), courseId);
            for (StudentAssignment assignment : assignments) {
                Double grade = assignment.getGrade();
                totalAssignmentScores += grade != null ? grade : 0.0;
                totalAssignments++;
            }
        }

        double averageAssignmentScore = totalAssignments > 0 ? (totalAssignmentScores / totalAssignments) : 0;
        reportData.put("averageAssignmentScore", averageAssignmentScore);

        return reportData;
    }

    public List<Map<String, Object>> generateQuizReport() {
        List<Course> courses = getAllCourses();
        List<Map<String, Object>> reportData = new ArrayList<>();
        for (Course course : courses) {
            Map<String, Object> courseData = generateQuizReport(course.getId());
            reportData.add(courseData);
        }
        return reportData;
    }

    public Map<String, Object> generateQuizReport(int courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course ID"));

        Map<String, Object> reportData = new HashMap<>();
        reportData.put("courseName", course.getTitle() != null ? course.getTitle() : "Unknown");

        List<Student> students = getStudentsInCourse(courseId);
        double totalQuizScores = 0.0;
        int totalQuizzes = 0;

        for (Student student : students) {
            List<StudentsQuizzes> quizzes = getStudentQuizzes(student.getId(), courseId);
            for (StudentsQuizzes quiz : quizzes) {
                Double grade = quiz.getGrade();
                totalQuizScores += grade != null ? grade : 0.0;
                totalQuizzes++;
            }
        }

        double averageQuizScore = totalQuizzes > 0 ? (totalQuizScores / totalQuizzes) : 0;
        reportData.put("averageQuizScore", averageQuizScore);

        return reportData;
    }

    public Map<String, Object> generateCombinedReport(int courseId) {
        Map<String, Object> attendanceData = generateAttendanceReport(courseId);
        Map<String, Object> assignmentData = generateAssignmentReport(courseId);
        Map<String, Object> quizData = generateQuizReport(courseId);

        Map<String, Object> combinedData = new HashMap<>();
        combinedData.put("courseName", attendanceData.get("courseName"));
        combinedData.put("averageAttendancePercentage", attendanceData.get("averageAttendancePercentage"));
        combinedData.put("averageAssignmentScore", assignmentData.get("averageAssignmentScore"));
        combinedData.put("averageQuizScore", quizData.get("averageQuizScore"));

        return combinedData;
    }

    public List<Map<String, Object>> generateChartReport() {
        List<Course> courses = getAllCourses();
        List<Map<String, Object>> reportData = new ArrayList<>();
        for (Course course : courses) {
            Map<String, Object> combinedData = generateCombinedReport(course.getId());
            reportData.add(combinedData);
        }
        return reportData;
    }

    public List<Map<String, Object>> generatePerformanceReport() {
        return generateChartReport();
    }
}