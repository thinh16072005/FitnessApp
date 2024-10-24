package repository;

import model.Coach;
import model.JDBC;
import utils.PasswordEncryption;

import java.sql.*;
import java.util.ArrayList;

public class CoachRepo {
    public boolean checkCoachIdExist(String coachId) {
        try {
            Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
            PreparedStatement prep = conn.prepareStatement("SELECT COUNT(*) FROM tblCoach WHERE CoachID = ?");
            prep.setString(1, coachId);
            ResultSet rs = prep.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            else {
                System.err.println("Coach ID does not exist!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean validateLogin(String coachId, String password) {
        try {
            String hashedPassword = PasswordEncryption.hashPassword(password);
            Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
            PreparedStatement prep = conn.prepareStatement("SELECT COUNT(*) FROM tblCoach WHERE CoachID = ? AND Password = ?");
            prep.setString(1, coachId);
            prep.setString(2, hashedPassword);
            ResultSet rs = prep.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }
        return false;
    }

    public Coach findCoachById(String coachId) {
        Coach coach = null;
        try {
            Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
            PreparedStatement prep = conn.prepareStatement("SELECT CoachID, CoachFirstName, CoachLastName, CoachEmail, CoachPhone FROM tblCoach WHERE CoachID = ?");
            prep.setString(1, coachId);
            ResultSet rs = prep.executeQuery();
            if (rs.next()) {
                coach = new Coach();
                coach.setCoachId(rs.getString("CoachID"));
                coach.setCoachFirstName(rs.getString("CoachFirstName"));
                coach.setCoachLastName(rs.getString("CoachLastName"));
                coach.setCoachEmail(rs.getString("CoachEmail"));
                coach.setCoachPhoneNumber(rs.getString("CoachPhone"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return coach;
    }


    private final ArrayList<String> coachList = new ArrayList<>();

    public ArrayList<String> getCoachList() {
        coachList.clear();
        try {
            Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
            PreparedStatement prep = conn.prepareStatement("SELECT * FROM tblCoach");
            ResultSet rs = prep.executeQuery();
            while (rs.next()) {
                coachList.add(rs.getString("CoachID"));
                coachList.add(rs.getString("CoachFirstName"));
                coachList.add(rs.getString("CoachLastName"));
                coachList.add(rs.getString("CoachEmail"));
                coachList.add(rs.getString("CoachPhone"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return coachList;
    }
}
