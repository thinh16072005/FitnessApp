package service;

import model.JDBC;
import repository.CourseRepo;
import repository.LearnerRepo;
import repository.SubscriptionRepo;
import utils.Utils;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class SubscriptionService {
    Scanner input = new Scanner(System.in);
    String enrollDate = LocalDate.now().toString();
    CourseService courseService = new CourseService();
    CourseRepo courseRepo = new CourseRepo();
    SubscriptionRepo subscriptionRepo = new SubscriptionRepo();
    LearnerRepo learnerRepo = new LearnerRepo();

    // ArrayList to store the time slots for each day of the week (0 = no slot)
    private final ArrayList<Integer> weeklySlots = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0));

    public void register(String learnerId) {
        courseService.display();
        String courseId = Utils.getString("Enter course ID: ", input);
        if (!courseRepo.checkCourseIdExist(courseId)) {
            System.out.println("Course ID does not exist!");
            return;
        }

        // Prompt user to select 3 slots for the week
        System.out.println("Please select 3 slots across the week for this course.");
        int slotsSelected = 0;
        while (slotsSelected < 3) {
            printDaySelectionMenu();
            int day = Utils.getInt("Select a day of the week (0 for Sunday, 1 for Monday, ...): ", input);
            if (day < 0 || day > 6 || weeklySlots.get(day) != 0) {
                System.out.println("Invalid selection or day already has a slot! Please try again.");
                continue;
            }
            int slot = getSlotSelection();
            weeklySlots.set(day, slot);
            slotsSelected++;
            System.out.println("You have selected slot " + slot + " for " + getDayOfWeek(day) + ".");
        }

        try {
            Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
            PreparedStatement prep = conn.prepareStatement("INSERT INTO tblSchedule (SubscriptionID, LearnerID, Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            prep.setString(1, autoGenerateSubscriptionID(conn));
            prep.setString(2, learnerId);
            // Setting slots for each day of the week
            prep.setInt(3, weeklySlots.get(0)); // Sunday
            prep.setInt(4, weeklySlots.get(1)); // Monday
            prep.setInt(5, weeklySlots.get(2)); // Tuesday
            prep.setInt(6, weeklySlots.get(3)); // Wednesday
            prep.setInt(7, weeklySlots.get(4)); // Thursday
            prep.setInt(8, weeklySlots.get(5)); // Friday
            prep.setInt(9, weeklySlots.get(6)); // Saturday
            prep.executeUpdate();
            System.out.println("Subscription added successfully.");
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }
    }

    private void printDaySelectionMenu() {
        System.out.println("Available days of the week:");
        for (int i = 0; i < weeklySlots.size(); i++) {
            String day = getDayOfWeek(i);
            String status = weeklySlots.get(i) == 0 ? "(No slot)" : "(Slot selected)";
            System.out.println(i + " - " + day + " " + status);
        }
    }

    public void viewWeeklySubscription(String learnerId, String courseName) {
        if (!learnerRepo.checkLearnerIdExist(learnerId)) {
            System.out.println("Learner ID does not exist!");
            return;
        }

        try {
            Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
            PreparedStatement prep = conn.prepareStatement("SELECT * FROM tblSchedule WHERE LearnerID = ?");
            prep.setString(1, learnerId);
            ResultSet rs = prep.executeQuery();

            if (rs.next()) {
                System.out.println("Weekly subscription slots for Learner ID: " + learnerId);
                System.out.println("+----------------------+----------------------+");
                System.out.println("| Day                  | Slot                 |");
                System.out.println("+----------------------+----------------------+");

                for (int i = 0; i < 7; i++) {
                    String day = getDayOfWeek(i);
                    int slot = rs.getInt(i + 3); // Columns for days start from the 3rd index
                    String slotInfo = (slot == 0) ? "" : "Slot " + slot + " (" + courseName + ")";
                    System.out.printf("| %-20s | %-20s |%n", day, slotInfo);
                }

                System.out.println("+----------------------+----------------------+");
            } else {
                System.out.println("No subscription found for Learner ID: " + learnerId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void unenroll() {
        String subscriptionId = Utils.getString("Enter subscription ID: ", input);
        if (!subscriptionRepo.checkSubscriptionIdExist(subscriptionId)) {
            System.out.println("Subscription ID does not exist!");
        } else {
            try {
                String confirm = Utils.getString("Are you sure? (Y/N): ", input);
                if (!confirm.equalsIgnoreCase("Y")) {
                    System.out.println("Subscription not deleted.");
                    return;
                } else {
                    Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
                    PreparedStatement prep = conn.prepareStatement("DELETE FROM tblSubscription WHERE SubscriptionID = ?");
                    prep.setString(1, subscriptionId);
                    prep.executeUpdate();
                    System.out.println("You have unenrolled from the course.");
                }
            } catch (SQLException e) {
                System.err.println("SQL Exception: " + e.getMessage());
            }
        }
    }

    private static String autoGenerateSubscriptionID(Connection connection) {
        String newSubscriptionId = "S001";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT MAX(SubscriptionID) AS maxID FROM tblSubscription");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String maxID = resultSet.getString("maxID");
                if (maxID != null) {
                    int idNumber = Integer.parseInt(maxID.substring(1)) + 1;
                    newSubscriptionId = String.format("S%03d", idNumber);
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }
        return newSubscriptionId;
    }

    private int getSlotSelection() {
        System.out.println("Available slots:");
        System.out.println("1. Slot 1: 7h-9h15");
        System.out.println("2. Slot 2: 9h30-11h45");
        System.out.println("3. Slot 3: 12h30-14h45");
        System.out.println("4. Slot 4: 15h-17h15");
        System.out.println("5. Slot 5: 17h30-19h45");
        System.out.println("6. Slot 6: 20h-22h");

        int slot = Utils.getInt("Select a slot (1-6): ", input);
        if (slot < 1 || slot > 6) {
            System.out.println("Invalid slot. Please select a valid slot (1-6).");
            return getSlotSelection();
        }
        return slot;
    }

    private String getDayOfWeek(int dayIndex) {
        String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        return days[dayIndex];
    }
}