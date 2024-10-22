package service;

import model.JDBC;
import repository.CourseRepo;
import utils.Utils;

import java.lang.reflect.Field;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class CourseService {
    CourseRepo courseRepo = new CourseRepo();
    Scanner scanner = new Scanner(System.in);

    public void add(String courseId) {
        String courseName = Utils.getString("Enter course name: ", scanner);
        String description = Utils.getString("Enter description: ", scanner);
        LocalDate startDate = LocalDate.parse(Utils.getString("Enter start date (dd/MM/yyyy): ", scanner), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        LocalDate endDate = LocalDate.parse(Utils.getString("Enter end date (dd/MM/yyyy): ", scanner), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        try {
            Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
            PreparedStatement prep = conn.prepareStatement("INSERT INTO tblCourse(CourseID, CourseName, CourseDescription, StartDate, EndDate, CoachID) VALUES (?, ?, ?, ?, ?, ?)");
            prep.setString(1, autoGenerateCourseID(conn));
            prep.setString(2, courseName);
            prep.setString(3, description);
            prep.setDate(4, Date.valueOf(startDate));
            prep.setDate(5, Date.valueOf(endDate));
            prep.setString(6, courseId);
            prep.executeUpdate();
            System.out.println("Course added");
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }
    }

    public void update() throws ClassNotFoundException {
    }

    public void delete() {
        String courseId = Utils.getString("Enter course ID: ", scanner);
        if (!courseRepo.checkCourseIdExist(courseId)) {
            System.out.println("Course ID does not exist!");
            return;
        }
        System.out.println("Course deleted");
    }

    public void display() {
        ArrayList<String> courseList = courseRepo.getCourseList();
        System.out.printf("%-10s %-30s %-15s %-25s %-15s %-10s %-10s%n" , "CourseID", "Course Name", "Description", "Duration", "Start Date", "End Date", "Coach ID");
        System.out.println("-----------------------------------------------------------------------------------------------");
        for (int i = 0; i < courseList.size(); i += 7) {
            System.out.printf("%-10s %-30s %-15s %-25s %-15s %-10s %-10s%n", courseList.get(i), courseList.get(i + 1), courseList.get(i + 2), courseList.get(i + 3), courseList.get(i + 4), courseList.get(i + 5), courseList.get(i + 6));
        }
    }

    public void displayLearners() {
        String courseId = Utils.getString("Enter course ID: ", scanner);
        if (!courseRepo.checkCourseIdExist(courseId)) {
            System.out.println("Course ID does not exist!");
            return;
        }

        try {
            Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
            PreparedStatement prep = conn.prepareStatement(
                    "SELECT LearnerID, LearnerFirstName, LearnerLastName, LearnerEmail, LearnerPhone, LearnerAge, LearnerDob " +
                            "FROM tblLearner " +
                            "WHERE LearnerID IN (SELECT LearnerID FROM tblSubscription WHERE CourseID = ?)"
            );
            prep.setString(1, courseId);
            ResultSet rs = prep.executeQuery();

            System.out.printf("%-10s %-15s %-15s %-25s %-15s %-5s %-10s%n", "LearnerID", "First Name", "Last Name", "Email", "Phone", "Age", "DOB");
            System.out.println("-----------------------------------------------------------------------------------------------");
            while (rs.next()) {
                System.out.printf("%-10s %-15s %-15s %-25s %-15s %-5d %-10s%n",
                        rs.getString("LearnerID"),
                        rs.getString("LearnerFirstName"),
                        rs.getString("LearnerLastName"),
                        rs.getString("LearnerEmail"),
                        rs.getString("LearnerPhone"),
                        rs.getInt("LearnerAge"),
                        rs.getDate("LearnerDob").toString()
                );
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
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
