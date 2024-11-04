package repository;

import model.JDBC;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
                exerciseList.add(rs.getString("ExerciseID"));
                exerciseList.add(rs.getString("ExerciseName"));
                exerciseList.add(rs.getString("ExerciseDuration"));
                exerciseList.add(rs.getString("Sets"));
                exerciseList.add(rs.getString("Reps"));
                exerciseList.add(rs.getString("CaloriesBurn"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exerciseList;
    }

    public List<String> getExercisesForCourse(String courseId) {
        List<String> exercises = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
            PreparedStatement prep = conn.prepareStatement("SELECT ExerciseID FROM tblExercise_Course WHERE CourseID = ?");
            prep.setString(1, courseId);
            ResultSet rs = prep.executeQuery();
            while (rs.next()) {
                exercises.add(rs.getString("ExerciseID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exercises;
    }

    public String getExerciseNameById(String id) {
        String exerciseName = "";
        try {
            Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
            PreparedStatement prep = conn.prepareStatement("SELECT ExerciseName FROM tblExercise WHERE ExerciseID = ?");
            prep.setString(1, id);
            ResultSet rs = prep.executeQuery();
            if (rs.next()) {
                exerciseName = rs.getString("ExerciseName");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exerciseName;
    }
}
