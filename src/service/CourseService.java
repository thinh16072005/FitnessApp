package service;

import model.Course;
import model.JDBC;
import repository.CoachRepo;
import repository.CourseRepo;
import utils.Utils;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class CourseService {
    CourseRepo courseRepo = new CourseRepo();
    Scanner scanner = new Scanner(System.in);
    CoachRepo coachRepo = new CoachRepo();


    public void add(String email) {
        String courseName = Utils.getString("Enter course name: ", scanner);
        String description = Utils.getString("Enter description: ", scanner);
        LocalDate startDate = LocalDate.parse(Utils.getValidStartDate("Enter start date (dd/MM/yyyy): "), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        LocalDate endDate = LocalDate.parse(Utils.getValidEndDate("Enter end date (dd/MM/yyyy): ", startDate), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        try {
            Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
            PreparedStatement prep = conn.prepareStatement("INSERT INTO tblCourse(CourseID, CourseName, CourseDescription, StartDate, EndDate, UserID) VALUES (?, ?, ?, ?, ?, ?)");
            prep.setString(1, autoGenerateCourseID(conn));
            prep.setString(2, courseName);
            prep.setString(3, description);
            prep.setDate(4, Date.valueOf(startDate));
            prep.setDate(5, Date.valueOf(endDate));
            prep.setString(6, coachRepo.getCoachIDByEmail(email));
            prep.executeUpdate();
            System.out.println("Course added");
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }
    }

    public void update() {
        String id = Utils.getString("Enter coach ID: ", scanner);
        try {
            System.out.println("Course found:");
            Course course = courseRepo.findCourseByEmail(id);
            System.out.println(course);

            String attribute = Utils.getString("\nEnter attribute (name of column) to update: ", scanner);
            String newValue = Utils.getString("Enter new value: ", scanner);

            try {
                Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
                PreparedStatement preparedStatement = conn.prepareStatement("UPDATE tblCourse SET " + attribute + " = ? WHERE CourseID = ?");
                preparedStatement.setString(1, newValue);
                preparedStatement.setString(2, id);
                preparedStatement.executeUpdate();
                System.out.println("Coach updated");
            } catch (SQLException e) {
                System.err.println("Exception: " + e.getMessage());
            }
        }
        catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
    }

    public void delete() {
        String courseId = Utils.getString("Enter course ID: ", scanner);
        Course course = courseRepo.findCourseByEmail(courseId);
        if (!courseRepo.checkCourseIdExist(courseId)) {
            System.out.println("Course ID does not exist!");
        } else {
            System.out.println(course);
            String confirm = Utils.getString("Are you sure? (Y/N): ", scanner);
            if (!confirm.equalsIgnoreCase("Y")) {
                System.out.println("Course not deleted");
            } else {
                try {
                    Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
                    PreparedStatement prep = conn.prepareStatement("DELETE FROM tblCourse WHERE CourseID = ?");
                    prep.setString(1, courseId);
                    prep.executeUpdate();
                    System.out.println("Course deleted");
                } catch (SQLException e) {
                    System.err.println("SQL Exception: " + e.getMessage());
                }
            }
        }
    }

    public void display() {
        ArrayList<String> courseList = courseRepo.getCourseList();
        System.out.printf("%-10s %-30s %-15s %-25s %-15s %-10s %-10s%n", "CourseID", "Course Name", "Description", "Duration (days)", "Start Date", "End Date", "Coach ID");
        System.out.println("-----------------------------------------------------------------------------------------------");
        for (int i = 0; i < courseList.size(); i += 7) {
            System.out.printf("%-10s %-30s %-15s %-25s %-15s %-10s %-10s%n", courseList.get(i), courseList.get(i + 1), courseList.get(i + 2), courseList.get(i + 3), courseList.get(i + 4), courseList.get(i + 5), courseList.get(i + 6));
        }
    }

    public void displayLearners(String coachId) {
        try {
            Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
            PreparedStatement prep = conn.prepareStatement(
                    "SELECT l.UserID, l.UserFirstName, l.UserLastName, l.UserEmail, l.UserPhone, l.UserAge, l.UserDOB " +
                            "FROM tblUser l " +
                            "JOIN tblSubscription s ON l.UserID = s.LearnerID " +
                            "JOIN tblCourse c ON s.CourseID = c.CourseID " +
                            "WHERE c.CoachID = ?");
            prep.setString(1, coachId);
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
                    int idNumber = Integer.parseInt(maxID.substring(2)) + 1;
                    newCourseId = String.format("CS%03d", idNumber);
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }
        return newCourseId;
    }
}
