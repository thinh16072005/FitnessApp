package service;

import repository.CourseRepo;
import repository.WorkoutRepo;
import utils.Utils;
import model.JDBC;
import model.Workout;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class WorkoutService {
    CourseRepo courseRepo = new CourseRepo();
    Scanner input = new Scanner(System.in);
    WorkoutRepo workoutRepo = new WorkoutRepo();

    public void add() {
        String courseId = Utils.getString("Enter course ID (Cxxx): ", input);
        if (!courseRepo.checkCourseIdExist(courseId)) {
            System.out.println("Course ID does not exist!");
        }
        else {
            String workoutName = Utils.getProperName("Enter workout name: ");

            try {
                Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
                PreparedStatement prep = conn.prepareStatement("INSERT INTO tblWorkout VALUES (?, ?, ?)");
                prep.setString(1, autoGenerateWorkoutID(conn));
                prep.setString(2, workoutName);
                prep.setString(3, courseId);
                prep.executeUpdate();
                System.out.println("Workout added");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    public void update() {
        Workout workout = new Workout();

        String workoutID = Utils.getProperString("Enter workout ID (Wxxx): ");
        if (!workoutRepo.checkWorkoutIdExist(workoutID)) {
            System.out.println("Workout ID does not exist!");
        }
        else {
            String attribute = Utils.getProperString("Enter attribute to update: ");
            String newValue = Utils.getProperString("Enter new value: ");
            try {
                Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
                PreparedStatement preparedStatement = conn.prepareStatement("UPDATE tblWorkout SET " + attribute + " = ? WHERE WorkoutID = ?");
                preparedStatement.setString(1, newValue);
                preparedStatement.setString(2, workoutID);
                preparedStatement.executeUpdate();
                System.out.println("Workout updated");
            } catch (SQLException e) {
                System.err.println("Exception: " + e.getMessage());
            }
        }
    }

    public void delete() {
        String workoutID = Utils.getProperString("Enter workout ID: ");
        if (!courseRepo.checkCourseIdExist(workoutID)) {
            System.out.println("Workout ID does not exist!");
        } else {
            try {
                String confirm = Utils.getString("Are you sure? (Y/N): ", input);
                if (!confirm.equalsIgnoreCase("Y")) {
                    System.out.println("Workout not deleted");
                    return;
                } else {
                    Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
                    PreparedStatement prep = conn.prepareStatement("DELETE FROM tblWorkout WHERE WorkoutID = ?");
                    prep.setString(1, workoutID);
                    prep.executeUpdate();
                    System.out.println("Workout deleted");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void display() {
        ArrayList<String> workoutList = workoutRepo.getWorkoutList();
        System.out.printf("%-10s %-15s %-15s%n", "LearnerID", "First Name", "Last Name");
        System.out.println("--------------------------------------------------------------------------------------");
        for (int i = 0; i < workoutList.size(); i += 3) {
            System.out.printf("%-10s %-15s %-15s%n", workoutList.get(i), workoutList.get(i + 1), workoutList.get(i + 2));
        }
    }

    private static String autoGenerateWorkoutID(Connection connection) {
        String newWorkoutID = "W001";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT MAX(WorkoutID) AS maxID FROM tblWorkout");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String maxID = resultSet.getString("maxID");
                if (maxID != null) {
                    int idNumber = Integer.parseInt(maxID.substring(1)) + 1;
                    newWorkoutID = String.format("W%03d", idNumber);
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }
        return newWorkoutID;
    }
}
