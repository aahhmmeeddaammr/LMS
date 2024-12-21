package com.LMS.LMS.Services;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class ChartReportService {
    public byte[] generateBarChartBytes(List<Map<String, Object>> reportData) throws IOException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Map<String, Object> data : reportData) {
            String courseName = (String) data.get("courseName");
            double AssignmentScore = data.get("averageAssignmentScore") == null ? 0 : (Double) data.get("averageAssignmentScore");
            System.out.println(AssignmentScore);
            double QuizScore = data.get("averageQuizScore") == null ? 0 : (Double) data.get("averageQuizScore");

            dataset.addValue(AssignmentScore, "Assignment Score", courseName);
            dataset.addValue(QuizScore, "Quiz Score", courseName);
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "Total Scores per Course",
                "Course Name",
                "Score",
                dataset
        );

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            ChartUtils.writeChartAsJPEG(bos, barChart, 800, 600);
            return bos.toByteArray();
        }
    }

    public byte[] generatePieChartBytes(List<Map<String, Object>> reportData) throws IOException {
        DefaultPieDataset dataset = new DefaultPieDataset();

        for (Map<String, Object> data : reportData) {
            String courseName = (String) data.get("courseName");
            double attendancePercentage = data.get("attendancePercentage") == null ? 0 : (Double) data.get("attendancePercentage");

            dataset.setValue(courseName, attendancePercentage);
        }

        JFreeChart pieChart = ChartFactory.createPieChart(
                "Attendance Percentage per Course",
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

}
