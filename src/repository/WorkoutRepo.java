package repository;

import model.JDBC;

import java.sql.*;
import java.util.ArrayList;

public class WorkoutRepo {
    private final ArrayList<String> workoutList = new ArrayList<>();

    public ArrayList<String> getWorkoutList() {
        workoutList.clear();
        try {
            Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
            PreparedStatement prep = conn.prepareStatement("SELECT * FROM tblWorkout");
            ResultSet rs = prep.executeQuery();
            while (rs.next()) {
                workoutList.add(rs.getString("WorkoutID"));
                workoutList.add(rs.getString("WorkoutName"));
                workoutList.add(rs.getString("CourseID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return workoutList;
    }

    public boolean validateLogin(String username, String password) {
        try {
            Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
            PreparedStatement prep = conn.prepareStatement("SELECT COUNT(*) FROM tblAdmin WHERE Username = ? AND Password = ?");
            prep.setString(1, username);
            prep.setString(2, password);
            ResultSet rs = prep.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }
        return false;
    }
}
