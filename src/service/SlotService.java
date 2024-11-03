package service;

import model.JDBC;
import model.Slot;
import repository.SlotRepo;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class SlotService {
    ExerciseService exerciseService = new ExerciseService();
    private SlotRepo slotRepo;

    public SlotService() {}

    public SlotService(SlotRepo slotRepo) {
        this.slotRepo = slotRepo;
    }

    public void generateSlotsAndDistributeExercises(String subscriptionId, String courseId, String schedule, String userId) {
        try (Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD)) {
            List<Slot> slots = new ArrayList<>();

            // Parse the input string (e.g., "7-9")
            String[] timeStrings = schedule.split("-");
            if (timeStrings.length != 2) {
                throw new IllegalArgumentException("Schedule format should be 'start-end' (e.g., '7-9').");
            }

            // Convert parsed strings to Time objects
            SimpleDateFormat sdf = new SimpleDateFormat("HH");
            Time timeStart = new Time(sdf.parse(timeStrings[0]).getTime());
            Time timeEnd = new Time(sdf.parse(timeStrings[1]).getTime());

            // Create and insert the slot into the database
            String slotId = generateSlotId(conn);
            Slot slot = new Slot(slotId, courseId, null, null, subscriptionId, timeStart, timeEnd);
            slots.add(slot);

            PreparedStatement insertSlot = conn.prepareStatement(
                    "INSERT INTO tblSlot (SlotID, CourseID, UserID, ExerciseID, ScheduleID, TimeStart, TimeEnd) VALUES (?, ?, ?, ?, ?, ?, ?)"
            );
            insertSlot.setString(1, slot.getSlotId());
            insertSlot.setString(2, slot.getCourseId());
            insertSlot.setString(3, userId);
            insertSlot.setString(4, slot.getExerciseId());
            insertSlot.setString(5, slot.getScheduleId());
            insertSlot.setTime(6, slot.getTimeStart());
            insertSlot.setTime(7, slot.getTimeEnd());
            insertSlot.executeUpdate();

            // Distribute exercises to the generated slots
            exerciseService.distributeExercisesToSlots(courseId);

            System.out.println("Slots generated and exercises distributed successfully.");
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        } catch (ParseException e) {
            System.err.println("Parse Exception: " + e.getMessage());
        }
    }

    // Method to display a slot's schedule and exercises by SlotID
    public void printSlotDetails(String slotId) {
        Slot slot = slotRepo.getSlotById(slotId);

        if (slot != null) {
            System.out.printf("Slot ID: %s%n", slot.getSlotId());
            System.out.printf("Time: %s - %s%n", slot.getTimeStart(), slot.getTimeEnd());

            System.out.println("Exercises:");
            String[] exercises = slot.getExerciseId().split(",");
            for (String exercise : exercises) {
                System.out.printf("- %s%n", exercise.trim());
            }
        } else {
            System.out.println("Slot not found!");
        }
    }

    // Method to display all slots and exercises for a specific workout
    public void printWorkoutSchedule(String workoutId) {
        List<Slot> slots = slotRepo.getSlotsByCourseId(workoutId);

        if (slots.isEmpty()) {
            System.out.printf("No slots found for Course ID: %s%n", workoutId);
        } else {
            System.out.printf("Workout Schedule for Course ID: %s%n", workoutId);
            System.out.println("+-------------------+----------------------+");
            System.out.println("| Time              | Exercises            |");
            System.out.println("++------------------+----------------------+");

            for (Slot slot : slots) {
                String exercises = String.join(", ", slot.getExerciseId().split(","));
                System.out.printf("| %-17s | %-20s |%n",
                        slot.getTimeStart() + " - " + slot.getTimeEnd(),
                        exercises);
            }
            System.out.println("+-------------+-------------------+----------------------+");
        }
    }

    private String generateSlotId(Connection connection) {
        String newSlotId = "SL001";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT MAX(ScheduleID) AS maxID FROM tblSchedule");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String maxID = resultSet.getString("maxID");
                if (maxID != null) {
                    int idNumber = Integer.parseInt(maxID.trim().substring(2)) + 1;
                    newSlotId = String.format("SC%03d", idNumber);
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }
        return newSlotId;
    }
}
