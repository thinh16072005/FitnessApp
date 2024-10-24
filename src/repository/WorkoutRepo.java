package repository;

import model.JDBC;
import model.Workout;

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

    public boolean checkWorkoutIdExist(String workoutId) {
        try {
            Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
            PreparedStatement prep = conn.prepareStatement("SELECT COUNT(*) FROM tblWorkout WHERE WorkoutID = ?");
            prep.setString(1, workoutId);
            ResultSet rs = prep.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            else {
                System.err.println("Workout ID does not exist!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Workout findWorkoutById(String workoutId) {
        Workout workout = null;
        try {
            Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
            PreparedStatement prep = conn.prepareStatement("SELECT * FROM tblWorkout WHERE WorkoutID = ?");
            prep.setString(1, workoutId);
            ResultSet rs = prep.executeQuery();
            if (rs.next()) {
                workout = new Workout();
                workout.setWorkoutID(rs.getString("WorkoutID"));
                workout.setWorkoutName(rs.getString("WorkoutName"));
                workout.setCourseId(rs.getString("CourseID"));
            }
            else {

                System.err.println("Workout ID does not exist!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return workout;
    }
}
