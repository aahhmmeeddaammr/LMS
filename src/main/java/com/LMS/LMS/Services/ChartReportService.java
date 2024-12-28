package com.LMS.LMS.Services;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class ChartReportService {
    public byte[] generatePieChartBytes(List<Map<String, Object>> reportData, String valueKey, String chartTitle) throws IOException {
        DefaultPieDataset dataset = new DefaultPieDataset();

        for (Map<String, Object> data : reportData) {
            String courseName = (String) data.get("courseName");
            Number value = data.get(valueKey) == null ? 0 : (Number) data.get(valueKey);
            dataset.setValue(courseName, value.doubleValue());
        }

        JFreeChart pieChart = ChartFactory.createPieChart(
                chartTitle,
                dataset,
                true,
                true,
                false
        );

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            ChartUtils.writeChartAsJPEG(bos, pieChart, 800, 600);
            return bos.toByteArray();
        }
    }

    public byte[] generateBarChartBytes(List<Map<String, Object>> reportData, String valueKey, String chartTitle, String categoryLabel, String valueLabel) throws IOException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Map<String, Object> data : reportData) {
            String courseName = (String) data.get("courseName");
            Number value = data.get(valueKey) == null ? 0 : (Number) data.get(valueKey);
            dataset.addValue(value.doubleValue(), valueLabel, courseName);
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                chartTitle,
                categoryLabel,
                valueLabel,
                dataset
        );

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            ChartUtils.writeChartAsJPEG(bos, barChart, 800, 600);
            return bos.toByteArray();
        }
    }

    // Method to combine multiple charts into one image
    public byte[] generateCombinedChartBytes(List<byte[]> chartImages) throws IOException {
        int width = 800;
        int height = 600;
        int combinedHeight = height * chartImages.size();

        BufferedImage combinedImage = new BufferedImage(width, combinedHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = combinedImage.createGraphics();

        int y = 0;
        for (byte[] imgData : chartImages) {
            ByteArrayInputStream bis = new ByteArrayInputStream(imgData);
            BufferedImage chartImage = ImageIO.read(bis);
            g.drawImage(chartImage, 0, y, null);
            y += height;
            bis.close();
        }
        g.dispose();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(combinedImage, "jpeg", bos);
        return bos.toByteArray();
    }
    public byte[] generateCombinedBarChartBytes(List<Map<String, Object>> combinedData) throws IOException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Map<String, Object> data : combinedData) {
            String courseName = (String) data.get("courseName");
            Number attendanceValue = data.get("averageAttendancePercentage") == null ? 0 : (Number) data.get("averageAttendancePercentage");
            Number assignmentValue = data.get("averageAssignmentScore") == null ? 0 : (Number) data.get("averageAssignmentScore");
            Number quizValue = data.get("averageQuizScore") == null ? 0 : (Number) data.get("averageQuizScore");

            dataset.addValue(attendanceValue.doubleValue(), "Attendance", courseName);
            dataset.addValue(assignmentValue.doubleValue(), "Assignments", courseName);
            dataset.addValue(quizValue.doubleValue(), "Quizzes", courseName);
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "Combined Average Scores per Course",
                "Course",
                "Average Score / Percentage",
                dataset
        );

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            ChartUtils.writeChartAsJPEG(bos, barChart, 1000, 600);
            return bos.toByteArray();
        }
    }
    public byte[] generatePieChartBytesForSingleCourse(List<Map<String, Object>> reportData, String valueKey, String chartTitle) throws IOException {
        DefaultPieDataset dataset = new DefaultPieDataset();

        for (Map<String, Object> data : reportData) {
            String category = (String) data.get("category");
            Number value = data.get(valueKey) == null ? 0 : (Number) data.get(valueKey);
            dataset.setValue(category, value.doubleValue());
        }

        JFreeChart pieChart = ChartFactory.createPieChart(
                chartTitle,
                dataset,
                true,
                true,
                false
        );

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            ChartUtils.writeChartAsJPEG(bos, pieChart, 800, 600);
            return bos.toByteArray();
        }
    }

    // Adjusted method for combined bar chart for a single course
    public byte[] generateCombinedBarChartBytesForSingleCourse(Map<String, Object> data) throws IOException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        String courseName = (String) data.get("courseName");

        Number attendanceValue = data.get("averageAttendancePercentage") == null ? 0 : (Number) data.get("averageAttendancePercentage");
        Number assignmentValue = data.get("averageAssignmentScore") == null ? 0 : (Number) data.get("averageAssignmentScore");
        Number quizValue = data.get("averageQuizScore") == null ? 0 : (Number) data.get("averageQuizScore");

        // Categories are metrics; series is the course name
        dataset.addValue(attendanceValue.doubleValue(), "Attendance Percentage", "Attendance");
        dataset.addValue(assignmentValue.doubleValue(), "Assignment Score", "Assignments");
        dataset.addValue(quizValue.doubleValue(), "Quiz Score", "Quizzes");

        JFreeChart barChart = ChartFactory.createBarChart(
                "Course Performance - " + courseName,
                "Performance Metrics",
                "Value",
                dataset
        );

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            ChartUtils.writeChartAsJPEG(bos, barChart, 800, 600);
            return bos.toByteArray();
        }
    }
}