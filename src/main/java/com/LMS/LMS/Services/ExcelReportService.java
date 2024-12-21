package com.LMS.LMS.Services;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class ExcelReportService {
    public byte[] generateExcelReport(List<Map<String, Object>> reportData) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Performance Report");

        // Create header
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Student Id");
        headerRow.createCell(1).setCellValue("Student Name");
        headerRow.createCell(2).setCellValue("Course Name");
        headerRow.createCell(3).setCellValue("Total Assignment Score");
        headerRow.createCell(4).setCellValue("Total Quiz Score");
        headerRow.createCell(5).setCellValue("Attendance Percentage");

        // Fill data
        int rowIndex = 1;
        for (Map<String, Object> data : reportData) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue((Integer) data.get("studentId")); // Student Name
            row.createCell(1).setCellValue((String) data.get("studentName")); // Student Name
            row.createCell(2).setCellValue((String) data.get("courseName")); // Course Name
            row.createCell(3).setCellValue(data.get("averageAssignmentScore") == null ? 0 : (Double) data.get("averageAssignmentScore")); // Average Assignment Score
            row.createCell(4).setCellValue(data.get("averageQuizScore") == null ? 0 : (Double) data.get("averageQuizScore")); // Average Quiz Score
            row.createCell(5).setCellValue(data.get("attendancePercentage") == null ? 0 : (Double) data.get("attendancePercentage")); // Attendance Percentage
        }
        // Write to output stream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }
}
