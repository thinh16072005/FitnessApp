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

    public void add() {
        String courseId = Utils.getString("Enter course ID: ", scanner);
        if (!courseRepo.checkCourseIdExist(courseId)) {
            System.out.println("Course ID does not exist!");
        }
        else {
            String workoutName = Utils.getProperName("Enter workout1 name: ");
            String nutrition = Utils.getProperName("Enter nutrition: ");
            double bmi = Utils.getPositiveDouble("Enter BMI", scanner);
            double calories = Utils.getPositiveDouble("Enter calories: ", scanner);
            try {
                Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
                PreparedStatement prep = conn.prepareStatement("INSERT INTO tblWorkout VALUES (?, ?, ?, ?, ?, ?)");
                prep.setString(1, autoGenerateWorkoutID(conn));
                prep.setString(2, workoutName);
                prep.setDouble(3, bmi);
                prep.setString(4, nutrition);
                prep.setDouble(5, calories);
                prep.setString(6, courseId);
                prep.executeUpdate();
                System.out.println("Workout added");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void update(Workout workout) throws ClassNotFoundException {
        String workoutID = Utils.getProperString("Enter workout ID: ");

        Class<?> workoutFields = Class.forName("Workout");

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
        try {
            Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
            PreparedStatement prep = conn.prepareStatement("DELETE FROM tblWorkout WHERE WorkoutID = ?");
            prep.setString(1, workoutID);
            prep.executeUpdate();
            System.out.println("Workout deleted");
        } catch (Exception e) {
            e.printStackTrace();
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
}
