package repository;

import model.Course;
import model.JDBC;
import model.Subscription;

import java.sql.*;
import java.time.LocalDate;
import java.util.Queue;
import java.util.PriorityQueue;

import java.util.Comparator;

public class SubscriptionRepo {

    public boolean checkSubscriptionIdExist(String subscriptionId) {
        try {
            Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
            PreparedStatement prep = conn.prepareStatement("SELECT * FROM tblSubscription WHERE SubscriptionID = ?");
            prep.setString(1, subscriptionId);
            ResultSet rs = prep.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private final Queue<String> subscriptionList = new PriorityQueue<>(Comparator.comparingInt(String::length));

    public Queue<String> getSubscriptionList() {
        subscriptionList.clear();
        try {
            Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
            PreparedStatement prep = conn.prepareStatement("SELECT * FROM tblSubscription");
            ResultSet rs = prep.executeQuery();
            while (rs.next()) {
                subscriptionList.add(rs.getString("SubscriptionID"));
                subscriptionList.add(rs.getString("LearnerID"));
                subscriptionList.add(rs.getString("CourseID"));
                subscriptionList.add(rs.getString("EnrollDate"));
                subscriptionList.add(rs.getString("Status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subscriptionList;
    }

    public String getCourseName(String subscriptionId) {
        String courseName = "";
        try {
            Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);

            // Query to get the course name using the subscription ID
            PreparedStatement prep = conn.prepareStatement(
                    "SELECT tblCourse.CourseName " +
                            "FROM tblCourse " +
                            "JOIN tblSubscription ON tblCourse.CourseID = tblSubscription.CourseID " +
                            "WHERE tblSubscription.SubscriptionID = ?");

            // Set the subscriptionId parameter in the query
            prep.setString(1, subscriptionId);

            // Execute the query and retrieve the result
            ResultSet rs = prep.executeQuery();
            if (rs.next()) {
                courseName = rs.getString("CourseName");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return courseName;
    }

    public String getSubscriptionId(String learnerId, String courseId) {
        String subscriptionId = null;
        try {
            Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
            PreparedStatement prep = conn.prepareStatement("SELECT SubscriptionID FROM tblSubscription WHERE LearnerID = ? AND CourseID = ?");
            prep.setString(1, learnerId);
            prep.setString(2, courseId);
            ResultSet rs = prep.executeQuery();
            if (rs.next()) {
                subscriptionId = rs.getString("SubscriptionID");
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }
        return subscriptionId;
    }
}
