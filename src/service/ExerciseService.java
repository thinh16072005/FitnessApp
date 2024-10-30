package service;

import model.JDBC;
import repository.ExerciseRepo;
import repository.WorkoutRepo;
import utils.Utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ExerciseService {
    Scanner input = new Scanner(System.in);
    ExerciseRepo exerciseRepo = new ExerciseRepo();
    WorkoutRepo workoutRepo = new WorkoutRepo();

    public void add() {
        String workoutID = Utils.getWorkoutId("Enter workout ID (Wxxx): ", input);
        if (!workoutRepo.checkWorkoutIdExist(workoutID)) {
            System.out.println("Workout ID does not exist!");
            return;
        }
        String exerciseName = Utils.getString("Enter exercise name: ", input);
        int duration = Utils.getInt("Enter duration: ", input);
        int set = Utils.getInt("Enter set: ", input);
        int rep = Utils.getInt("Enter rep: ", input);
        double calories = Utils.getDouble("Enter calories: ", input);

        String confirm = Utils.getString("Is this information correct? (Y/N): ", input);
        if (!confirm.equalsIgnoreCase("Y")) {
            System.out.println("Exercise not added");
        }
        else {
            try {
                Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
                PreparedStatement prep = conn.prepareStatement("INSERT INTO tblExercise VALUES (?, ?, ?, ?, ?, ?)");
                prep.setString(1, autoGenerateExerciseID(conn));
                prep.setString(2, exerciseName);
                prep.setInt(3, duration);
                prep.setInt(4, set);
                prep.setInt(5, rep);
                prep.setDouble(6, calories);
                prep.executeUpdate();
                System.out.println("\nExercise added");
            } catch (SQLException e) {
                System.err.println("SQL Exception: " + e.getMessage());
            }
        }
    }

    public void delete() {
        String exerciseID = Utils.getExerciseId("Enter exercise ID: ", input);
        if (!exerciseRepo.checkExerciseIdExist(exerciseID)) {
            System.out.println("Exercise ID does not exist!");
            return;
        }
        else {
            try {
                Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
                PreparedStatement prep = conn.prepareStatement("DELETE FROM tblExercise WHERE ExerciseID = ?");
                prep.setString(1, exerciseID);
                prep.executeUpdate();
                System.out.println("Exercise deleted");
            } catch (SQLException e) {
                System.err.println("SQL Exception: " + e.getMessage());
            }
        }
    }

    public void update() {
        String exerciseID = Utils.getExerciseId("Enter exercise ID: ", input);
        if (!exerciseRepo.checkExerciseIdExist(exerciseID)) {
            System.out.println("Exercise ID does not exist!");
        }
        else {
            try {
                String attribute = Utils.getString("Enter attribute to update: ", input);
                String newValue = Utils.getString("Enter new value: ", input);
                Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
                PreparedStatement prep = conn.prepareStatement("UPDATE tblExercise SET " + attribute + " = ? WHERE ExerciseID = ?");
                prep.setString(1, newValue);
                prep.setString(2, exerciseID);
                prep.executeUpdate();
                System.out.println("Exercise updated");
            } catch (SQLException e) {
                System.err.println("SQL Exception: " + e.getMessage());
            }
        }
    }

    public void display() {
        ArrayList<String> exerciseList = exerciseRepo.getExerciseList();
        System.out.printf("%-10s %-20s %-20s %-25s %-15s %-10s %-20s%n", "Exercise ID", "Exercise Name", "Duration (mins)", "Workout ID", "Sets", "Reps", "Calories");
        System.out.println("-----------------------------------------------------------------------------------------------");
        for (int i = 0; i < exerciseList.size(); i += 7) {
            System.out.printf("%-10s %-20s %-20s %-25s %-15s %-15s %-20s%n", exerciseList.get(i), exerciseList.get(i + 1), exerciseList.get(i + 2), exerciseList.get(i + 3), exerciseList.get(i + 4), exerciseList.get(i + 5), exerciseList.get(i + 6));
        }
    }


    private static String autoGenerateExerciseID(Connection connection) {
        String newExerciseID = "E001";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT MAX(ExerciseID) AS maxID FROM tblExercise");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String maxID = resultSet.getString("maxID");
                if (maxID != null) {
                    int idNumber = Integer.parseInt(maxID.substring(2)) + 1;
                    newExerciseID = String.format("EX%03d", idNumber);
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }
        return newExerciseID;
    }
}
