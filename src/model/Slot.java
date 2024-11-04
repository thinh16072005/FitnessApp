package model;

import java.sql.*;

public class Slot {
    private String slotId;
    private String courseId;
    private String learnerId;
    private String exerciseId;
    private String scheduleId;
    private Time timeStart;
    private Time timeEnd;

    public Slot() {
    }
    public Slot(String slotId, String courseId, String learnerId, String exerciseId, String scheduleId, Time timeStart, Time timeEnd) {
        this.slotId = slotId;
        this.exerciseId = exerciseId;
        this.courseId = courseId;
        this.learnerId = learnerId;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
    }

    public String getSlotId() {
        return slotId;
    }

    public void setSlotId(String slotId) {
        this.slotId = slotId;
    }

    public String getExerciseId() {
//        String exerciseId = "";
//        try {
//            Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
//            PreparedStatement prep = conn.prepareStatement("SELECT ExerciseID FROM tblExercise");
//            ResultSet rs = prep.executeQuery();
//            if (rs.next()) {
//                exerciseId = rs.getString("ExerciseID");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        return exerciseId;
    }

    public void setExerciseId(String exerciseId) {
        this.exerciseId = exerciseId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getLearnerId() {
        return learnerId;
    }

    public void setLearnerId(String learnerId) {
        this.learnerId = learnerId;
    }

    public String getScheduleId() {
        String scheduleId = "";
        try {
            Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
            PreparedStatement prep = conn.prepareStatement("SELECT ScheduleID FROM tblSchedule");
            ResultSet rs = prep.executeQuery();
            if (rs.next()) {
                scheduleId = rs.getString("ScheduleID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Time getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Time timeStart) {
        this.timeStart = timeStart;
    }

    public Time getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Time timeEnd) {
        this.timeEnd = timeEnd;
    }
}
