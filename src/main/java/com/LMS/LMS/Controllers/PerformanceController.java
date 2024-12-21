package com.LMS.LMS.Controllers;

import com.LMS.LMS.Services.ChartReportService;
import com.LMS.LMS.Services.ExcelReportService;
import com.LMS.LMS.Services.PerformanceService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
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
            List<Map<String, Object>> reportData = performanceService.generatePerformanceReport();
            byte[] excelBytes = excelReportService.generateExcelReport(reportData);
            // Set response headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "performance_report.xlsx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelBytes);

        } catch (Exception e) {
            // Log the error (optional: implement logging)
            return ResponseEntity.internalServerError()
                    .body(("Error generating Excel report: " + e.getMessage()).getBytes());
        }
    }

    @GetMapping("charts/bar")
    public ResponseEntity<byte[]> downloadBarChart() throws IOException {
        List<Map<String, Object>> reportData = performanceService.generatePerformanceReport();
        byte[] imageBytes = chartReportService.generateBarChartBytes(reportData);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentDispositionFormData("attachment", "BarChart.jpeg");

        return ResponseEntity.ok()
                .headers(headers)
                .body(imageBytes);
    }

    @GetMapping("charts/pie")
    public ResponseEntity<byte[]> downloadPieChart() throws IOException {
        List<Map<String, Object>> reportData = performanceService.generatePerformanceReport();
        byte[] imageBytes = chartReportService.generatePieChartBytes(reportData);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentDispositionFormData("attachment", "PieChart.jpeg");

        return ResponseEntity.ok()
                .headers(headers)
                .body(imageBytes);
    }
}