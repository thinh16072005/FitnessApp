package repository;

import model.JDBC;

import java.sql.*;
import java.util.ArrayList;

public class ExerciseRepo {
    private final ArrayList<String> exerciseList = new ArrayList<>();

    public boolean checkExerciseIdExist(String courseId) {
        try {
            Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
            PreparedStatement prep = conn.prepareStatement("SELECT COUNT(*) FROM tblExercise WHERE ExerciseID = ?");
            prep.setString(1, courseId);
            ResultSet rs = prep.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            else {
                System.err.println("Course ID does not exist!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<String> getExerciseList() {
        exerciseList.clear();
        try {
            Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
            PreparedStatement prep = conn.prepareStatement("SELECT * FROM tblExercise");
            ResultSet rs = prep.executeQuery();
            while (rs.next()) {
                exerciseList.add(rs.getString("CourseID"));
                exerciseList.add(rs.getString("CourseName"));
                exerciseList.add(rs.getString("CourseDescription"));
                exerciseList.add(rs.getString("CourseDuration"));
                exerciseList.add(rs.getString("StartDate"));
                exerciseList.add(rs.getString("EndDate"));
                exerciseList.add(rs.getString("CoachID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exerciseList;
    }
}
