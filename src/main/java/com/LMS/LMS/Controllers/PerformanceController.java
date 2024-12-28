package com.LMS.LMS.Controllers;

import com.LMS.LMS.Services.ChartReportService;
import com.LMS.LMS.Services.ExcelReportService;
import com.LMS.LMS.Services.PerformanceService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/performance")
public class PerformanceController {
    private final PerformanceService performanceService;
    private final ExcelReportService excelReportService;
    private final ChartReportService chartReportService;

    public PerformanceController(PerformanceService performanceService, ExcelReportService excelReportService, ChartReportService chartReportService) {
        this.performanceService = performanceService;
        this.excelReportService = excelReportService;
        this.chartReportService = chartReportService;
    }

    @GetMapping("/download-excel-report")
    public ResponseEntity<byte[]> downloadPerformanceReport() throws IOException {
        try {
            List<Map<String, Object>> reportData = performanceService.generateAllCoursesPerformanceReport();
            byte[] excelBytes = excelReportService.generateExcelReportForAllCourses(reportData);
            // Set response headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "performance_report.xlsx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelBytes);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(("Error generating Excel report: " + e.getMessage()).getBytes());
        }
    }
    @GetMapping("/download-excel-report/{id}")
    public ResponseEntity<byte[]> downloadPerformanceReport(@PathVariable int id) throws IOException {
        try {
            Map<String, Object> reportData = performanceService.generateCoursePerformanceReport(id);
            byte[] excelBytes = excelReportService.generateExcelReportForCourse(reportData);
            // Set response headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "performance_report.xlsx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelBytes);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(("Error generating Excel report: " + e.getMessage()).getBytes());
        }
    }
    @GetMapping("charts/pie/attendance")
    public ResponseEntity<byte[]> downloadAttendancePieChart() throws IOException {
        List<Map<String, Object>> reportData = performanceService.generateAttendanceReport();
        byte[] imageBytes = chartReportService.generatePieChartBytes(reportData, "averageAttendancePercentage", "Average Attendance Percentage per Course");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentDispositionFormData("attachment", "AttendancePieChart.jpeg");

        return ResponseEntity.ok()
                .headers(headers)
                .body(imageBytes);
    }

    @GetMapping("charts/pie/assignments")
    public ResponseEntity<byte[]> downloadAssignmentPieChart() throws IOException {
        List<Map<String, Object>> reportData = performanceService.generateAssignmentReport();
        byte[] imageBytes = chartReportService.generatePieChartBytes(reportData, "averageAssignmentScore", "Average Assignment Score per Course");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentDispositionFormData("attachment", "AssignmentPieChart.jpeg");

        return ResponseEntity.ok()
                .headers(headers)
                .body(imageBytes);
    }

    @GetMapping("charts/pie/quizzes")
    public ResponseEntity<byte[]> downloadQuizPieChart() throws IOException {
        List<Map<String, Object>> reportData = performanceService.generateQuizReport();
        byte[] imageBytes = chartReportService.generatePieChartBytes(reportData, "averageQuizScore", "Average Quiz Score per Course");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentDispositionFormData("attachment", "QuizPieChart.jpeg");

        return ResponseEntity.ok()
                .headers(headers)
                .body(imageBytes);
    }

    @GetMapping("charts/pie/combined")
    public ResponseEntity<byte[]> downloadCombinedPieChart() throws IOException {
        // Generate individual charts
        List<byte[]> chartImages = new ArrayList<>();

        List<Map<String, Object>> attendanceData = performanceService.generateAttendanceReport();
        byte[] attendanceChart = chartReportService.generatePieChartBytes(attendanceData, "averageAttendancePercentage", "Average Attendance Percentage per Course");
        chartImages.add(attendanceChart);

        List<Map<String, Object>> assignmentData = performanceService.generateAssignmentReport();
        byte[] assignmentChart = chartReportService.generatePieChartBytes(assignmentData, "averageAssignmentScore", "Average Assignment Score per Course");
        chartImages.add(assignmentChart);

        List<Map<String, Object>> quizData = performanceService.generateQuizReport();
        byte[] quizChart = chartReportService.generatePieChartBytes(quizData, "averageQuizScore", "Average Quiz Score per Course");
        chartImages.add(quizChart);

        // Combine the charts
        byte[] combinedChart = chartReportService.generateCombinedChartBytes(chartImages);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentDispositionFormData("attachment", "CombinedPieChart.jpeg");

        return ResponseEntity.ok()
                .headers(headers)
                .body(combinedChart);
    }

    // Similar endpoints for bar charts
    @GetMapping("charts/bar/attendance")
    public ResponseEntity<byte[]> downloadAttendanceBarChart() throws IOException {
        List<Map<String, Object>> reportData = performanceService.generateAttendanceReport();
        byte[] imageBytes = chartReportService.generateBarChartBytes(reportData, "averageAttendancePercentage", "Average Attendance Percentage per Course", "Course", "Attendance Percentage");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentDispositionFormData("attachment", "AttendanceBarChart.jpeg");

        return ResponseEntity.ok()
                .headers(headers)
                .body(imageBytes);
    }

    @GetMapping("charts/bar/assignments")
    public ResponseEntity<byte[]> downloadAssignmentBarChart() throws IOException {
        List<Map<String, Object>> reportData = performanceService.generateAssignmentReport();
        byte[] imageBytes = chartReportService.generateBarChartBytes(reportData, "averageAssignmentScore", "Average Assignment Score per Course", "Course", "Assignment Score");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentDispositionFormData("attachment", "AssignmentBarChart.jpeg");

        return ResponseEntity.ok()
                .headers(headers)
                .body(imageBytes);
    }

    @GetMapping("charts/bar/quizzes")
    public ResponseEntity<byte[]> downloadQuizBarChart() throws IOException {
        List<Map<String, Object>> reportData = performanceService.generateQuizReport();
        byte[] imageBytes = chartReportService.generateBarChartBytes(reportData, "averageQuizScore", "Average Quiz Score per Course", "Course", "Quiz Score");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentDispositionFormData("attachment", "QuizBarChart.jpeg");

        return ResponseEntity.ok()
                .headers(headers)
                .body(imageBytes);
    }

    @GetMapping("charts/bar/combined")
    public ResponseEntity<byte[]> downloadCombinedBarChart() throws IOException {

        // Prepare datasets
        List<Map<String, Object>> attendanceData = performanceService.generateAttendanceReport();
        List<Map<String, Object>> assignmentData = performanceService.generateAssignmentReport();
        List<Map<String, Object>> quizData = performanceService.generateQuizReport();

        // Merge datasets into one for combined bar chart
        // Assuming all datasets have the same courses in the same order
        List<Map<String, Object>> combinedData = new ArrayList<>();
        for (int i = 0; i < attendanceData.size(); i++) {
            Map<String, Object> combinedEntry = new HashMap<>();
            combinedEntry.put("courseName", attendanceData.get(i).get("courseName"));
            combinedEntry.put("averageAttendancePercentage", attendanceData.get(i).get("averageAttendancePercentage"));
            combinedEntry.put("averageAssignmentScore", assignmentData.get(i).get("averageAssignmentScore"));
            combinedEntry.put("averageQuizScore", quizData.get(i).get("averageQuizScore"));
            combinedData.add(combinedEntry);
        }

        byte[] imageBytes = chartReportService.generateCombinedBarChartBytes(combinedData);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentDispositionFormData("attachment", "CombinedBarChart.jpeg");

        return ResponseEntity.ok()
                .headers(headers)
                .body(imageBytes);
    }
    @GetMapping("charts/pie/attendance/{courseId}")
    public ResponseEntity<byte[]> downloadAttendancePieChart(@PathVariable int courseId) throws IOException {
        Map<String, Object> reportData = performanceService.generateAttendanceReport(courseId);

        // Since it's a single data point, transform it
        List<Map<String, Object>> reportDataList = new ArrayList<>();
        reportDataList.add(reportData);

        byte[] imageBytes = chartReportService.generatePieChartBytes(
                reportDataList,
                "averageAttendancePercentage",
                "Attendance Percentage - " + reportData.get("courseName")
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        String filename = "AttendancePieChart_" + reportData.get("courseName") + ".jpeg";
        headers.setContentDispositionFormData("attachment", filename);

        return ResponseEntity.ok().headers(headers).body(imageBytes);
    }

    @GetMapping("charts/pie/assignments/{courseId}")
    public ResponseEntity<byte[]> downloadAssignmentPieChart(@PathVariable int courseId) throws IOException {
        Map<String, Object> reportData = performanceService.generateAssignmentReport(courseId);

        List<Map<String, Object>> reportDataList = new ArrayList<>();
        reportDataList.add(reportData);

        byte[] imageBytes = chartReportService.generatePieChartBytes(
                reportDataList,
                "averageAssignmentScore",
                "Assignment Score - " + reportData.get("courseName")
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        String filename = "AssignmentPieChart_" + reportData.get("courseName") + ".jpeg";
        headers.setContentDispositionFormData("attachment", filename);

        return ResponseEntity.ok().headers(headers).body(imageBytes);
    }

    @GetMapping("charts/pie/quizzes/{courseId}")
    public ResponseEntity<byte[]> downloadQuizPieChart(@PathVariable int courseId) throws IOException {
        Map<String, Object> reportData = performanceService.generateQuizReport(courseId);

        List<Map<String, Object>> reportDataList = new ArrayList<>();
        reportDataList.add(reportData);

        byte[] imageBytes = chartReportService.generatePieChartBytes(
                reportDataList,
                "averageQuizScore",
                "Quiz Score - " + reportData.get("courseName")
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        String filename = "QuizPieChart_" + reportData.get("courseName") + ".jpeg";
        headers.setContentDispositionFormData("attachment", filename);

        return ResponseEntity.ok().headers(headers).body(imageBytes);
    }

    // Combined Pie Chart for a Specific Course
    @GetMapping("charts/pie/combined/{courseId}")
    public ResponseEntity<byte[]> downloadCombinedPieChart(@PathVariable int courseId) throws IOException {
        Map<String, Object> combinedData = performanceService.generateCombinedReport(courseId);

        // Transform the combined data into a list suitable for the pie chart
        List<Map<String, Object>> reportData = new ArrayList<>();

        reportData.add(Map.of("category", "Attendance", "value", combinedData.get("averageAttendancePercentage")));
        reportData.add(Map.of("category", "Assignments", "value", combinedData.get("averageAssignmentScore")));
        reportData.add(Map.of("category", "Quizzes", "value", combinedData.get("averageQuizScore")));

        byte[] imageBytes = chartReportService.generatePieChartBytesForSingleCourse(
                reportData,
                "value",
                "Course Performance - " + combinedData.get("courseName")
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        String filename = "CombinedPieChart_" + combinedData.get("courseName") + ".jpeg";
        headers.setContentDispositionFormData("attachment", filename);

        return ResponseEntity.ok().headers(headers).body(imageBytes);
    }

    // Bar Charts for a Specific Course
    @GetMapping("charts/bar/attendance/{courseId}")
    public ResponseEntity<byte[]> downloadAttendanceBarChart(@PathVariable int courseId) throws IOException {
        Map<String, Object> reportData = performanceService.generateAttendanceReport(courseId);

        List<Map<String, Object>> reportDataList = new ArrayList<>();
        reportDataList.add(reportData);

        byte[] imageBytes = chartReportService.generateBarChartBytes(
                reportDataList,
                "averageAttendancePercentage",
                "Attendance Percentage - " + reportData.get("courseName"),
                "Course",
                "Attendance Percentage"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        String filename = "AttendanceBarChart_" + reportData.get("courseName") + ".jpeg";
        headers.setContentDispositionFormData("attachment", filename);

        return ResponseEntity.ok().headers(headers).body(imageBytes);
    }

    @GetMapping("charts/bar/assignments/{courseId}")
    public ResponseEntity<byte[]> downloadAssignmentBarChart(@PathVariable int courseId) throws IOException {
        Map<String, Object> reportData = performanceService.generateAssignmentReport(courseId);

        List<Map<String, Object>> reportDataList = new ArrayList<>();
        reportDataList.add(reportData);

        byte[] imageBytes = chartReportService.generateBarChartBytes(
                reportDataList,
                "averageAssignmentScore",
                "Assignment Score - " + reportData.get("courseName"),
                "Course",
                "Assignment Score"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        String filename = "AssignmentBarChart_" + reportData.get("courseName") + ".jpeg";
        headers.setContentDispositionFormData("attachment", filename);

        return ResponseEntity.ok().headers(headers).body(imageBytes);
    }

    @GetMapping("charts/bar/quizzes/{courseId}")
    public ResponseEntity<byte[]> downloadQuizBarChart(@PathVariable int courseId) throws IOException {
        Map<String, Object> reportData = performanceService.generateQuizReport(courseId);

        List<Map<String, Object>> reportDataList = new ArrayList<>();
        reportDataList.add(reportData);

        byte[] imageBytes = chartReportService.generateBarChartBytes(
                reportDataList,
                "averageQuizScore",
                "Quiz Score - " + reportData.get("courseName"),
                "Course",
                "Quiz Score"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        String filename = "QuizBarChart_" + reportData.get("courseName") + ".jpeg";
        headers.setContentDispositionFormData("attachment", filename);

        return ResponseEntity.ok().headers(headers).body(imageBytes);
    }

    // Combined Bar Chart for a Specific Course
    @GetMapping("charts/bar/combined/{courseId}")
    public ResponseEntity<byte[]> downloadCombinedBarChart(@PathVariable int courseId) throws IOException {
        Map<String, Object> combinedData = performanceService.generateCombinedReport(courseId);

        byte[] imageBytes = chartReportService.generateCombinedBarChartBytesForSingleCourse(combinedData);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        String filename = "CombinedBarChart_" + combinedData.get("courseName") + ".jpeg";
        headers.setContentDispositionFormData("attachment", filename);

        return ResponseEntity.ok().headers(headers).body(imageBytes);
    }
}