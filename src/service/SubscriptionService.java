package service;

import model.JDBC;
import repository.CourseRepo;
import repository.SubscriptionRepo;
import utils.Utils;

import java.sql.*;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.Queue;
import java.util.Scanner;

public class SubscriptionService {
    Scanner input = new Scanner(System.in);
    String enrollDate = LocalDate.now().toString();
    CourseService courseService = new CourseService();
    CourseRepo courseRepo = new CourseRepo();
    SubscriptionRepo subscriptionRepo = new SubscriptionRepo();

    public void register(String learnerId) {
        courseService.display();
        String courseId = Utils.getString("Enter course ID: ", input);
        if (!courseRepo.checkCourseIdExist(courseId)) {
            System.out.println("Course ID does not exist!");
            return;
        }
        try {
             Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
             PreparedStatement prep = conn.prepareStatement("INSERT INTO tblSubscription VALUES (?, ?, ?, ?, ?)");
             prep.setString(1, autoGenerateSubscriptionID(conn));
             prep.setString(2, learnerId);
             prep.setString(3, courseId);
             prep.setString(4, enrollDate);
             prep.setString(5, "Active");
             prep.executeUpdate();
             System.out.println("Subscription added");
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }
    }

    public void unenroll() {
        String subscriptionId = Utils.getString("Enter subscription ID: ", input);
        if (!subscriptionRepo.checkSubscriptionIdExist(subscriptionId)) {
            System.out.println("Subscription ID does not exist!");
            return;
        }
        else {
            try {
                String confirm = Utils.getString("Are you sure? (Y/N): ", input);
                if (!confirm.equalsIgnoreCase("Y")) {
                    System.out.println("Subscription not deleted");
                    return;
                }
                else {
                    Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
                    PreparedStatement prep = conn.prepareStatement("DELETE FROM tblSubscription WHERE SubscriptionID = ?");
                    prep.setString(1, subscriptionId);
                    prep.executeUpdate();
                    System.out.println("You have unenrolled from the course");
                }

            } catch (SQLException e) {
                System.err.println("SQL Exception: " + e.getMessage());
            }
        }
    }

    public void display() {
        Queue<String> subscriptionList = subscriptionRepo.getSubscriptionList();
        Iterator<String> iterator = subscriptionList.iterator();
        while (iterator.hasNext()) {
            System.out.printf("%-10s %-15s %-15s %-25s %-15s%n", iterator.next(), iterator.next(), iterator.next(), iterator.next(), iterator.next());
        }
    }

    private static String autoGenerateSubscriptionID(Connection connection) {
        String newCourseId = "S001";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT MAX(SubscriptionID) AS maxID FROM tblSubscription");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String maxID = resultSet.getString("maxID");
                if (maxID != null) {
                    int idNumber = Integer.parseInt(maxID.substring(1)) + 1;
                    newCourseId = String.format("S%03d", idNumber);
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }
        return newCourseId;
    }
}
