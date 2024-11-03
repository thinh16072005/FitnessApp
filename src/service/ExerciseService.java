package service;

import model.JDBC;
import model.Slot;
import repository.ExerciseRepo;
import utils.Utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ExerciseService {
    Scanner input = new Scanner(System.in);
    ExerciseRepo exerciseRepo = new ExerciseRepo();

    public void add() {

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
        System.out.printf("%-10s %-20s %-20s %-15s %-10s %-20s%n", "Exercise ID", "Exercise Name", "Duration (mins)", "Sets", "Reps", "Calories");
        System.out.println("-----------------------------------------------------------------------------------------------");
        for (int i = 0; i < exerciseList.size(); i += 6) {
            System.out.printf("%-10s %-20s %-20s %-15s %-15s %-20s%n", exerciseList.get(i), exerciseList.get(i + 1), exerciseList.get(i + 2), exerciseList.get(i + 3), exerciseList.get(i + 4), exerciseList.get(i + 5));
        }
    }


    public void ExerciseToCourse() {
        String courseId = Utils.getString("Enter Course ID: ", input);
        ArrayList<String> exerciseIds = new ArrayList<>();

        while (true) {
            String exerciseId = Utils.getString("Enter Exercise ID (or type 'done' to finish): ", input);
            if (exerciseId.equalsIgnoreCase("done")) {
                break;
            }
            if (!exerciseRepo.checkExerciseIdExist(exerciseId)) {
                System.out.println("Exercise ID does not exist!");
            } else {
                exerciseIds.add(exerciseId);
            }
        }

        if (exerciseIds.isEmpty()) {
            System.out.println("No exercises to add.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD)) {
            PreparedStatement prep = conn.prepareStatement("INSERT INTO tblExercise_Course (CourseID, ExerciseID) VALUES (?, ?)");
            for (String exerciseId : exerciseIds) {
                prep.setString(1, courseId);
                prep.setString(2, exerciseId);
                prep.addBatch();
            }
            prep.executeBatch();
            System.out.println("Exercises added to course successfully.");
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
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

    public void distributeExercisesToSlots(String courseId) {
        try (Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD)) {
            // Retrieve all exercises for the given course
            PreparedStatement getExercises = conn.prepareStatement("SELECT ExerciseID FROM tblExercise_Course WHERE CourseID = ?");
            getExercises.setString(1, courseId);
            ResultSet exercisesRs = getExercises.executeQuery();

            List<String> exerciseIds = new ArrayList<>();
            while (exercisesRs.next()) {
                exerciseIds.add(exercisesRs.getString("ExerciseID"));
            }

            if (exerciseIds.isEmpty()) {
                System.out.println("No exercises found for the given course.");
                return;
            }

            // Retrieve all slots for the given course
            PreparedStatement getSlots = conn.prepareStatement("SELECT SlotID, TimeStart, TimeEnd FROM tblSlot WHERE CourseID = ?");
            getSlots.setString(1, courseId);
            ResultSet slotsRs = getSlots.executeQuery();

            List<Slot> slots = new ArrayList<>();
            while (slotsRs.next()) {
                String slotId = slotsRs.getString("SlotID");
                Time timeStart = slotsRs.getTime("TimeStart");
                Time timeEnd = slotsRs.getTime("TimeEnd");
                slots.add(new Slot(slotId, null, null, null, null, timeStart, timeEnd));
            }

            if (slots.isEmpty()) {
                System.out.println("No slots found for the given course.");
                return;
            }

            // Distribute exercises to slots
            int exerciseIndex = 0;
            for (Slot slot : slots) {
                if (exerciseIndex >= exerciseIds.size()) {
                    break;
                }

                String exerciseId = exerciseIds.get(exerciseIndex);
                slot.setExerciseId(exerciseId);

                // Update the slot with the exercise ID
                PreparedStatement updateSlot = conn.prepareStatement("UPDATE tblSlot SET ExerciseID = ? WHERE SlotID = ?");
                updateSlot.setString(1, exerciseId);
                updateSlot.setString(2, slot.getSlotId());
                updateSlot.executeUpdate();

                exerciseIndex++;
            }

            System.out.println("Exercises distributed to slots successfully.");
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }
    }
}
