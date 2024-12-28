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

    public byte[] generateExcelReportForCourse(Map<String, Object> courseReportData) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        createCourseSheet(workbook, courseReportData);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        return outputStream.toByteArray();
    }

    public byte[] generateExcelReportForAllCourses(List<Map<String, Object>> allCoursesReportData) throws IOException {
        Workbook workbook = new XSSFWorkbook();

        for (Map<String, Object> courseReportData : allCoursesReportData) {
            createCourseSheet(workbook, courseReportData);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        return outputStream.toByteArray();
    }

    private void createCourseSheet(Workbook workbook, Map<String, Object> courseReportData) {
        String courseName = (String) courseReportData.get("courseName");
        Sheet sheet = workbook.createSheet(courseName != null ? courseName : "Unknown Course");

        List<Map<String, Object>> assignmentsInfo = (List<Map<String, Object>>) courseReportData.get("assignmentsInfo");
        List<Map<String, Object>> quizzesInfo = (List<Map<String, Object>>) courseReportData.get("quizzesInfo");

        // Create Header Row
        Row headerRow = sheet.createRow(0);
        int cellIndex = 0;
        headerRow.createCell(cellIndex++).setCellValue("Student ID");
        headerRow.createCell(cellIndex++).setCellValue("Student Name");
        headerRow.createCell(cellIndex++).setCellValue("Attendance Percentage");

        for (Map<String, Object> assignmentInfo : assignmentsInfo) {
            Integer assignmentId = (Integer) assignmentInfo.get("id");
            Double maxMarks = (Double) assignmentInfo.get("maxMarks");
            headerRow.createCell(cellIndex++).setCellValue("Assignment " + assignmentId + " (" + maxMarks + ")");
        }

        for (Map<String, Object> quizInfo : quizzesInfo) {
            Integer quizId = (Integer) quizInfo.get("id");
            Double maxMarks = (Double) quizInfo.get("maxMarks");
            headerRow.createCell(cellIndex++).setCellValue("Quiz " + quizId + " (" + maxMarks + ")");
        }

        headerRow.createCell(cellIndex++).setCellValue("Total Assignment Score");
        headerRow.createCell(cellIndex++).setCellValue("Total Quiz Score");
        headerRow.createCell(cellIndex++).setCellValue("Total Score");

        List<Map<String, Object>> studentDataList = (List<Map<String, Object>>) courseReportData.get("students");
        int rowIndex = 1;
        for (Map<String, Object> studentData : studentDataList) {
            Row row = sheet.createRow(rowIndex++);
            cellIndex = 0;
            row.createCell(cellIndex++).setCellValue((Integer) studentData.get("studentId"));
            row.createCell(cellIndex++).setCellValue((String) studentData.get("studentName"));
            row.createCell(cellIndex++).setCellValue((Double) studentData.get("attendancePercentage"));

            Map<Integer, Double> assignmentGrades = (Map<Integer, Double>) studentData.get("assignmentGrades");
            for (Map<String, Object> assignmentInfo : assignmentsInfo) {
                Integer assignmentId = (Integer) assignmentInfo.get("id");
                Double grade = assignmentGrades.get(assignmentId);
                row.createCell(cellIndex++).setCellValue(grade != null ? grade : 0);
            }

            Map<Integer, Double> quizGrades = (Map<Integer, Double>) studentData.get("quizGrades");
            for (Map<String, Object> quizInfo : quizzesInfo) {
                Integer quizId = (Integer) quizInfo.get("id");
                Double grade = quizGrades.get(quizId);
                row.createCell(cellIndex++).setCellValue(grade != null ? grade : 0);
            }

            row.createCell(cellIndex++).setCellValue((Double) studentData.get("totalAssignmentScore"));
            row.createCell(cellIndex++).setCellValue((Double) studentData.get("totalQuizScore"));
            row.createCell(cellIndex++).setCellValue((Double) studentData.get("totalScore"));
        }

        // Auto-size columns for readability
        int totalColumns = cellIndex;
        for (int i = 0; i < totalColumns; i++) {
            sheet.autoSizeColumn(i);
        }
    }
}