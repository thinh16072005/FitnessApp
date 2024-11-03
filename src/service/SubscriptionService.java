package service;

import model.JDBC;
import model.Slot;
import repository.CourseRepo;
import repository.LearnerRepo;
import repository.SubscriptionRepo;
import utils.Utils;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class SubscriptionService {
    Scanner input = new Scanner(System.in);
    String enrollDate = LocalDate.now().toString();
    CourseService courseService = new CourseService();
    ExerciseService exerciseService = new ExerciseService();
    CourseRepo courseRepo = new CourseRepo();
    SubscriptionRepo subscriptionRepo = new SubscriptionRepo();
    LearnerRepo learnerRepo = new LearnerRepo();
    SlotService slotService = new SlotService();

    private final ArrayList<Integer> weeklySlots = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0));

    public void register(String learnerEmail) {
        int totalExercises = 0;
        courseService.display();
        String courseId = Utils.getString("Enter course ID (CSxxx): ", input);
        if (!courseRepo.checkCourseIdExist(courseId)) {
            System.out.println("Course ID does not exist!");
            return;
        }
        // Determine Intensity Level (number of sessions per week)
        int sessionsPerWeek = getIntensityLevel();

        // Determine Stamina Level (duration per session)
        int sessionDuration = getStaminaLevel();

        // Retrieve the number of exercises for the course;
        try (Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD)) {
            PreparedStatement prepared = conn.prepareStatement("SELECT COUNT(ExerciseID) FROM tblExercise");
            ResultSet rs = prepared.executeQuery();
            if (rs.next()) {
                totalExercises = rs.getInt(1);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        int slotsNeeded = totalExercises / sessionsPerWeek;  // Calculate number of slots

        // Register daily schedule
        System.out.println("Please schedule your sessions for the week.");
        Map<String, String> weeklySchedule = new HashMap<>();
        for (int i = 0; i < sessionsPerWeek; i++) {
            String dayOfWeek = getDaySelection();
            String timeSlot = Utils.getString("Enter time slot (e.g., '7-9' for 7 am to 9 am): ", input);
            weeklySchedule.put(dayOfWeek, timeSlot);
            System.out.println("Session scheduled for " + dayOfWeek + " at " + timeSlot);
        }

        String platform = Utils.getPlatform("Enter platform (Online / Offline): ", input);

        String confirm = Utils.getString("Confirm registration? (Y/N): ", input);
        if (!confirm.equalsIgnoreCase("Y")) {
            System.out.println("Subscription not added.");
            return;
        }

        // Insert subscription and schedule into the database
        try (Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD)) {
            String subscriptionId = autoGenerateSubscriptionID(conn);

            // Insert subscription data into tblSubscription
            PreparedStatement prepared = conn.prepareStatement("INSERT INTO tblSubscription (SubscriptionID, UserID, CourseID, EnrollDate, Platform, Status) VALUES (?, ?, ?, ?, ?, ?)");
            prepared.setString(1, subscriptionId);
            prepared.setString(2, learnerRepo.getLearnerIDByEmail(learnerEmail));
            prepared.setString(3, courseId);
            prepared.setString(4, enrollDate);
            prepared.setString(5, platform);
            prepared.setString(6, subscriptionRepo.getSubscriptionStatus(subscriptionId));
            prepared.executeUpdate();

            // Insert weekly schedule data into tblSchedule
            PreparedStatement prep = conn.prepareStatement("INSERT INTO tblSchedule (ScheduleID, SubscriptionID, Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            prep.setString(1, autoGenerateScheduleID(conn));
            prep.setString(2, subscriptionId);
            for (int i = 0; i < 7; i++) {
                String day = getDayOfWeek(i);
                String time = weeklySchedule.getOrDefault(day, "");
                prep.setString(i + 3, time); // Days columns start from the 3rd index
            }
            prep.executeUpdate();

            // Generate slots and distribute exercises based on the schedule and calculated slotsNeeded
            System.out.println(slotsNeeded);
            for (Map.Entry<String, String> entry : weeklySchedule.entrySet()) {
                for (int j = 0; j < slotsNeeded; j++) {
                    slotService.generateSlotsAndDistributeExercises(subscriptionId, courseId, entry.getValue(), learnerRepo.getLearnerIDByEmail(learnerEmail));
                }
            }

            System.out.println("Subscription and schedule added successfully.");
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }
    }

    private int getIntensityLevel() {
        System.out.println("Select your intensity level:");
        System.out.println("1 - Beginner (3 sessions per week)");
        System.out.println("2 - Getting Comfortable (4 sessions per week)");
        System.out.println("3 - Intermediate (5 sessions per week)");
        System.out.println("4 - Experienced (6 sessions per week)");
        System.out.println("5 - Advanced (7 sessions per week)");

        int level = Utils.getInt("Enter your intensity level (1-5): ", input);
        return switch (level) {
            case 1 -> 3;
            case 2 -> 4;
            case 3 -> 5;
            case 4 -> 6;
            case 5 -> 7;
            default -> 3; // Default to Beginner if input is invalid
        };
    }

    private int getStaminaLevel() {
        System.out.println("Select your stamina level:");
        System.out.println("1 - Beginner (1 hour per session)");
        System.out.println("2 - Experienced (2 hours per session)");
        System.out.println("3 - Advanced (3 hours per session)");

        int level = Utils.getInt("Enter your stamina level (1-3): ", input);
        return switch (level) {
            case 1 -> 1;
            case 2 -> 2;
            case 3 -> 3;
            default -> 1; // Default to Beginner if input is invalid
        };
    }

    private String getDaySelection() {
        System.out.println("Select a day for your session:");
        System.out.println("0 - Sunday");
        System.out.println("1 - Monday");
        System.out.println("2 - Tuesday");
        System.out.println("3 - Wednesday");
        System.out.println("4 - Thursday");
        System.out.println("5 - Friday");
        System.out.println("6 - Saturday");

        int dayIndex = Utils.getInt("Enter the day of the week (0-6): ", input);
        return getDayOfWeek(dayIndex);
    }

    private String getDayOfWeek(int dayIndex) {
        String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        return dayIndex >= 0 && dayIndex < 7 ? days[dayIndex] : "Unknown";
    }

    private void printDaySelectionMenu() {
        System.out.println("Available days of the week:");
        for (int i = 0; i < weeklySlots.size(); i++) {
            String day = getDayOfWeek(i);
            String status = weeklySlots.get(i) == 0 ? "(No slot)" : "(Slot selected)";
            System.out.println(i + " - " + day + " " + status);
        }
    }

    public void viewWeeklySubscription(String email, String courseName) {
        if (!learnerRepo.checkLearnerExist(email)) {
            System.out.println("Learner email does not exist!");
            return;
        }
        try (Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD)) {
            PreparedStatement prep = conn.prepareStatement("SELECT * FROM tblSchedule WHERE SubscriptionID = ?");
            prep.setString(1, subscriptionRepo.getSubscriptionId(email, courseName));
            ResultSet rs = prep.executeQuery();

            if (rs.next()) {
                System.out.printf("Weekly subscription slots for Learner %s%n:", learnerRepo.getLearnerFirstName(email));
                System.out.println("+----------------------+----------------------+");
                System.out.println("| Day                  | Slot                 |");
                System.out.println("+----------------------+----------------------+");
                for (int i = 0; i < 7; i++) {
                    String day = getDayOfWeek(i);
                    String slot = rs.getString(i + 3);
                    String slotInfo = (slot == null || slot.isEmpty()) ? "" : "Slot " + slot + " (" + courseName + ")";
                    System.out.printf("| %-20s | %-20s |%n", day, slotInfo);
                }
                System.out.println("+----------------------+----------------------+");
            } else {
                System.out.printf("No subscription found for Learner: %s%n", email);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void unenroll() {
        Queue<String> subscriptionQueue = subscriptionRepo.getSubscriptionList();
        if (subscriptionQueue.isEmpty()) {
            System.out.println("No active subscriptions.");
            return;
        }
        System.out.println("List of subscription IDs:");
        subscriptionQueue.forEach(System.out::println);

        String subscriptionId = Utils.getString("Enter Subscription ID to unenroll: ", input);
        if (!subscriptionRepo.checkSubscriptionIdExist(subscriptionId)) {
            System.out.println("Subscription ID does not exist!");
            return;
        }
        String confirm = Utils.getString("Confirm unenrollment? (Y/N): ", input);
        if (!confirm.equalsIgnoreCase("Y")) {
            System.out.println("Unenrollment cancelled.");
            return;
        }
        try (Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD)) {
            PreparedStatement prep = conn.prepareStatement("DELETE FROM tblSubscription WHERE SubscriptionID = ?");
            prep.setString(1, subscriptionId);
            prep.executeUpdate();
            System.out.println("Unenrollment successful.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getSlotSelection() {
        System.out.println("Available slots (morning = 1, afternoon = 2, evening = 3): ");
        return Utils.getInt("Choose a slot: ", input);
    }



    private String autoGenerateScheduleID(Connection connection) {
        String newScheduleId = "SC001";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT MAX(ScheduleID) AS maxID FROM tblSchedule");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String maxID = resultSet.getString("maxID");
                if (maxID != null) {
                    int idNumber = Integer.parseInt(maxID.trim().substring(2)) + 1;
                    newScheduleId = String.format("SC%03d", idNumber);
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }
        return newScheduleId;
    }

    private String autoGenerateSubscriptionID(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT COUNT(SubscriptionID) AS count FROM tblSubscription");
        int nextId = rs.next() ? rs.getInt("count") + 1 : 1;
        return String.format("SB%03d", nextId);
    }
}
