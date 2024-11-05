package service;

import model.JDBC;
import model.Slot;
import repository.ExerciseRepo;
import repository.SlotRepo;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class SlotService {
    ExerciseService exerciseService = new ExerciseService();
    ExerciseRepo exerciseRepo = new ExerciseRepo();
    SlotRepo slotRepo;
    CourseService courseService;

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
            insertSlot.setString(1,generateSlotId(conn));
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

    // Helper function to insert the slot into the database
    private void insertSlotIntoDatabase(Connection conn, Slot slot, String userId) throws SQLException {
        PreparedStatement insertSlotStmt = conn.prepareStatement(
                "INSERT INTO tblSlot (SlotID, CourseID, UserID, ExerciseID, ScheduleID, TimeStart, TimeEnd) VALUES (?, ?, ?, ?, ?, ?, ?)"
        );
        insertSlotStmt.setString(1, generateSlotId(conn));
        insertSlotStmt.setString(2, slot.getCourseId());
        insertSlotStmt.setString(3, userId);
        insertSlotStmt.setString(4, slot.getExerciseId());
        insertSlotStmt.setString(5, slot.getScheduleId());
        insertSlotStmt.setTime(6, slot.getTimeStart());
        insertSlotStmt.setTime(7, slot.getTimeEnd());
        insertSlotStmt.executeUpdate();
    }



    // Method to display a slot's schedule and exercises by SlotID
    public void printSlotDetails(String slotId) {
        Slot slot = slotRepo.getSlotById(slotId);
        if (slot != null) {
//            System.out.printf("Slot ID: %s%n", slot.getSlotId());
            System.out.printf("Time: %s - %s%n", slot.getTimeStart(), slot.getTimeEnd());
            System.out.println("Exercises:");

            String[] exerciseIds = slot.getExerciseId().split(",");
            for (String exerciseId : exerciseIds) {
                String exerciseName = exerciseRepo.getExerciseNameById(exerciseId.trim());
                if (exerciseName != null) {
                    System.out.printf("- %s (ID: %s)%n", exerciseName, exerciseId.trim());
                } else {
                    System.out.printf("- Exercise with ID %s not found%n", exerciseId.trim());
                }
            }
        } else {
            System.out.println("Slot not found!");
        }
    }

    // Method to display all slots and exercises for a specific workout
    public void printWorkoutSchedule(String courseId) {
        Queue<Slot> slotQueue = new LinkedList<>(slotRepo.getSlotsByCourseId(courseId));

        if (slotQueue.isEmpty()) {
            System.out.printf("No slots found for Course ID: %s%n", courseId);
            return;
        }

        System.out.printf("Workout Schedule for Course ID: %s%n", courseId);
        System.out.println("+------------+------------+----------------------+----------------------+");
        System.out.println("| Week      | Day        | Time                | Exercises            |");
        System.out.println("+------------+------------+----------------------+----------------------+");

        int slotNumber = 1;
        int currentWeek = 1;

        try (Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD)) {
            String scheduleQuery = """
            SELECT s.Sunday, s.Monday, s.Tuesday, s.Wednesday, s.Thursday, s.Friday, s.Saturday
            FROM tblSchedule s
            JOIN tblSubscription sub ON s.subscriptionID = sub.subscriptionID
            JOIN tblCourse c ON sub.courseID = c.courseID
            WHERE c.courseID = ?
            """;

            PreparedStatement stmt = conn.prepareStatement(scheduleQuery);
            stmt.setString(1, courseId);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                System.out.println("No schedule found for the specified course ID.");
                return;
            }

            String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

            while (!slotQueue.isEmpty()) {
                System.out.printf("| Week %d      |            |                      |                      |%n", currentWeek);

                for (String dayOfWeek : daysOfWeek) {
                    String timeRange = rs.getString(dayOfWeek);

                    if (timeRange == null || !timeRange.matches("\\d+-\\d+")) {
                        continue; // Skip this day if there's no valid time
                    }

                    Slot slot = slotQueue.poll(); // Dequeue the next slot only if the day has a valid time
                    if (slot == null) {
                        break;
                    }

                    // Get exercise names associated with the slot
                    String[] exerciseIds = slot.getExerciseId().split(",");
                    String exercises = Arrays.stream(exerciseIds)
                            .map(id -> exerciseRepo.getExerciseNameById(id.trim()))
                            .collect(Collectors.joining(", "));

                    // Print the slot details
                    System.out.printf("| %-10d | %-10s | %-20s | %-20s |%n", slotNumber, dayOfWeek, timeRange, exercises);
                    slotNumber++;
                }
                currentWeek++;
            }

        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }

        System.out.println("+------------+------------+----------------------+----------------------+");
    }


    // Helper function to get day name from an offset (0 for Sunday, 6 for Saturday)
    private String getDayName(int dayOffset) {
        String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        return days[dayOffset];
    }

    public String generateSlotId(Connection connection) {
        String newSlotId = "SL001";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT MAX(SlotID) AS maxID FROM tblSlot");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String maxID = resultSet.getString("maxID");
                if (maxID != null) {
                    int idNumber = Integer.parseInt(maxID.trim().substring(2)) + 1;
                    newSlotId = String.format("SL%03d", idNumber);
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }
        return newSlotId;
    }
}
