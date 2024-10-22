package service;

import repository.CourseRepo;
import utils.Utils;
import model.JDBC;
import model.Workout;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.Scanner;

public class WorkoutService {
    Scanner scanner = new Scanner(System.in);
    CourseRepo courseRepo = new CourseRepo();
    Scanner input = new Scanner(System.in);

    public void add() {
        String courseId = Utils.getString("Enter course ID: ", scanner);
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

    public void addExercise() {
        String workoutID = Utils.getWorkoutId("Enter workout ID: ", input);
        String exerciseName = Utils.getExerciseId("Enter exercise name: ", input);
        int duration = Utils.getInt("Enter duration: ", input);
        int set = Utils.getInt("Enter set: ", input);
        int rep = Utils.getInt("Enter rep: ", input);
        double calories = Utils.getDouble("Enter calories: ", input);
        String confirm = Utils.getString("Is this information correct? (Y/N): ", input);
        if (!confirm.equalsIgnoreCase("Y")) {
            System.out.println("Exercise not added");
            return;
        }
        else {
            try {
                Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
                PreparedStatement prep = conn.prepareStatement("INSERT INTO tblExercise VALUES (?, ?, ?, ?, ?, ?, ?)");
                prep.setString(1, autoGenerateExerciseID(conn));
                prep.setString(2, exerciseName);
                prep.setInt(3, duration);
                prep.setString(4, workoutID);
                prep.setInt(5, set);
                prep.setInt(6, rep);
                prep.setDouble(7, calories);
                prep.executeUpdate();
                System.out.println("\nExercise added");
            } catch (SQLException e) {
                System.err.println("SQL Exception: " + e.getMessage());
            }
        }

    }

    public void update(Workout workout) throws ClassNotFoundException {
        String workoutID = Utils.getProperString("Enter workout ID: ");

        Class<?> workoutFields = Class.forName("model.Workout");

        Field[] fields = workoutFields.getSuperclass().getDeclaredFields();
        for (Field field : fields) {
            System.out.print("\t" + field.getName());
        }

        String attribute = Utils.getProperString("Enter attribute to update: ");
        String newValue = Utils.getProperString("Enter new value: ");
        try {
            Field field = workout.getClass().getDeclaredField(attribute);
            field.setAccessible(true);
            field.set(workout, newValue);

            Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
            PreparedStatement preparedStatement = conn.prepareStatement("UPDATE tblWorkout SET " + attribute + " = ? WHERE WorkoutID = ?");
            preparedStatement.setString(1, newValue);
            preparedStatement.setString(2, workoutID);
            preparedStatement.executeUpdate();
            System.out.println("Workout updated");
        } catch (NoSuchFieldException | IllegalAccessException | SQLException e) {
            System.err.println("Exception: " + e.getMessage());
        }
    }

    public void delete() {
        String workoutID = Utils.getProperString("Enter workout ID: ");
        if (!courseRepo.checkCourseIdExist(workoutID)) {
            System.out.println("Workout ID does not exist!");
            return;
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
