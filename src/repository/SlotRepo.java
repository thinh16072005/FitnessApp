package repository;

import model.JDBC;
import model.Slot;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SlotRepo {

    // Method to retrieve a slot by SlotID
    public Slot getSlotById(String slotId) {
        Slot slot = null;
        try (Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD)) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM tblSlot WHERE SlotID = ?");
            stmt.setString(1, slotId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                slot = new Slot(
                        rs.getString("SlotID"),
                        rs.getString("CourseID"),
                        rs.getString("UserID"),
                        rs.getString("ExerciseID"),
                        rs.getString("ScheduleID"),
                        rs.getTime("TimeStart"),
                        rs.getTime("TimeEnd")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return slot;
    }

    // Method to retrieve all slots for a given WorkoutID
    public List<Slot> getSlotsByCourseId(String courseId) {
        List<Slot> slots = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD)) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM tblSlot WHERE CourseID = ?");
            stmt.setString(1, courseId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                slots.add(new Slot(
                        rs.getString("SlotID"),
                        rs.getString("CourseID"),
                        rs.getString("UserID"),
                        rs.getString("ExerciseID"),
                        rs.getString("ScheduleID"),
                        rs.getTime("TimeStart"),
                        rs.getTime("TimeEnd")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return slots;
    }

    public String getSlotIdByCourseId(String courseId) {
        String slotId = "";
        try {
            Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
            PreparedStatement prep = conn.prepareStatement("SELECT SlotID FROM tblSlot WHERE CourseID = ?");
            prep.setString(1, courseId);
            ResultSet rs = prep.executeQuery();
            if (rs.next()) {
                slotId = rs.getString("SlotID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return slotId;
    }

    // Method to insert a new slot into the database
    public void insertSlot(Slot slot) {
        try (Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD)) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO tblSlot (SlotID, CourseID, UserID, ExerciseID, ScheduleID, TimeStart, TimeEnd) VALUES (?, ?, ?, ?, ?, ?, ?)");
            stmt.setString(1, slot.getSlotId());
            stmt.setString(2, slot.getCourseId());
            stmt.setString(3, slot.getLearnerId());
            stmt.setString(4, slot.getExerciseId());
            stmt.setString(5, slot.getScheduleId());
            stmt.setTime(6, slot.getTimeStart());
            stmt.setTime(7, slot.getTimeEnd());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    public String getSubscriptionIdByCourseId(String courseId) {
//        String subscriptionId = "";
//        try {
//            Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
//            PreparedStatement prep = conn.prepareStatement("SELECT SubscriptionID FROM tblSubscription WHERE CourseID = ?");
//            prep.setString(1, courseId);
//            ResultSet rs = prep.executeQuery();
//            if (rs.next()) {
//                subscriptionId = rs.getString("SubscriptionID");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return subscriptionId;
//    }

    // Method to update an existing slot
//    public void updateSlot(Slot slot) {
//        try (Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD)) {
//            PreparedStatement stmt = conn.prepareStatement("UPDATE tblSlot SET ExerciseID = ?, CourseID = ?, LearnerID = ?, ExerciseID = ?, ScheduleID = ?, TimeStart = ?, TimeEnd = ? WHERE SlotID = ?");
//            stmt.setString(1, slot.getSlotId());
//            stmt.setString(2, slot.getCourseId());
//            stmt.setString(3, slot.getLearnerId());
//            stmt.setString(4, slot.getExerciseId());
//            stmt.setString(5, slot.getScheduleId());
//            stmt.setTime(6, slot.getTimeStart());
//            stmt.setTime(7, slot.getTimeEnd());
//            stmt.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
}