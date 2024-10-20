package service;

import model.Course;
import model.JDBC;
import utils.Utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class CourseService {
    Scanner scanner = new Scanner(System.in);

    public void add(Course course, String courseId) {
        String courseName = Utils.getProperName("Enter course name: ");
        String description = Utils.getString("Enter description: ", scanner);
        String startDate = Utils.getValidDate("Enter start date: ");
        String endDate = Utils.getValidDate("Enter end date: ");
        int duration = Utils.calculateDuration(startDate, endDate);
        try {
            Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
            PreparedStatement prep = conn.prepareStatement("INSERT INTO tblCourse VALUES (?, ?, ?, ?, ?, ?, ?)");
            prep.setString(1, autoGenerateCourseID(conn));
            prep.setString(2, courseName);
            prep.setString(3, description);
            prep.setInt(4, duration);
            prep.setString(5, startDate);
            prep.setString(6, endDate);
            prep.setString(7, courseId);
            prep.executeUpdate();
            System.out.println("Course added");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(Course course) throws ClassNotFoundException {
        System.out.println("Course updated");
    }


    public void delete(Course course) {
        System.out.println("Course deleted");
    }


    public void display() {
        ArrayList<String> courseList = new ArrayList<>();
        System.out.printf("%-10s %-15s %-15s %-25s %-15s %-10s %-10s" , "CourseID", "Course Name", "Description", "Duration", "Start Date", "End Date", "Coach ID");
        System.out.println("--------------------------------------------------------------------------------------");
        for (int i = 0; i < courseList.size(); i += 5) {
            System.out.printf("%-10s %-15s %-15s %-25s %-15s%n", courseList.get(i), courseList.get(i + 1), courseList.get(i + 2), courseList.get(i + 3), courseList.get(i + 4), courseList.get(i + 5), courseList.get(i + 6));
        }
    }

    private static String autoGenerateCourseID(Connection connection) {
        String newCourseId = "CS001";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT MAX(CourseID) AS maxID FROM tblCourse");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String maxID = resultSet.getString("maxID");
                if (maxID != null) {
                    int idNumber = Integer.parseInt(maxID.substring(1)) + 1;
                    newCourseId = String.format("CS%03d", idNumber);
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }
        return newCourseId;
    }
}
