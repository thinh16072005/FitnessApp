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
            PreparedStatement prep = conn.prepareStatement("SELECT COUNT(*) FROM tblUser WHERE UserRole = 'Coach' AND UserID = ?");
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

    public String getCoachIDByEmail(String email) {
        String coachId = "";
        try {
            Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
            PreparedStatement prep = conn.prepareStatement("SELECT UserID FROM tblUser WHERE UserRole = 'Coach' AND UserEmail = ?");
            prep.setString(1, email);
            ResultSet rs = prep.executeQuery();
            if (rs.next()) {
                coachId = rs.getString("UserID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return coachId;
    }

    public Coach findCoachById(String email) {
        Coach coach = null;
        try {
            Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
            PreparedStatement prep = conn.prepareStatement("SELECT UserID, UserFirstName, UserLastName, UserEmail, UserPhone, UserDOB, UserAge FROM tblUser WHERE UserEmail = ?");
            prep.setString(1, email);
            ResultSet rs = prep.executeQuery();
            if (rs.next()) {
                coach = new Coach();
                coach.setCoachId(rs.getString("UserID"));
                coach.setCoachFirstName(rs.getString("UserFirstName"));
                coach.setCoachLastName(rs.getString("UserLastName"));
                coach.setCoachEmail(rs.getString("UserEmail"));
                coach.setCoachPhoneNumber(rs.getString("UserPhone"));
                coach.setCoachDob(rs.getDate("UserDOB"));
                coach.setCoachAge(rs.getInt("UserAge"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return coach;
    }

    public String getCoachFirstName(String email) {
        String coachName = "";
        try {
            Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
            PreparedStatement prep = conn.prepareStatement("SELECT UserFirstName FROM tblUser WHERE UserRole = 'Coach' AND UserEmail = ?");
            prep.setString(1, email);
            ResultSet rs = prep.executeQuery();
            if (rs.next()) {
                coachName = rs.getString("UserFirstName");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return coachName;
    }


    private final ArrayList<String> coachList = new ArrayList<>();

    public ArrayList<String> getCoachList() {
        coachList.clear();
        try {
            Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
            PreparedStatement prep = conn.prepareStatement("SELECT * FROM tblUser WHERE UserRole = 'Coach'");
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
