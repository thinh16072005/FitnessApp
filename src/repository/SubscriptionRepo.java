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
    Course course = new Course();
    Subscription subscription = new Subscription();

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
}
