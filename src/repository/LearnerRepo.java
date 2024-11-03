package repository;

import model.JDBC;
import model.Learner;
import utils.PasswordEncryption;

import java.sql.*;
import java.util.ArrayList;

public class LearnerRepo {
    public boolean checkLearnerExist(String email) {
        try {
            Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
            PreparedStatement prep = conn.prepareStatement("SELECT COUNT(*) FROM tblUser WHERE UserRole = ? AND UserEmail = ?");
            prep.setString(1, "Learner");
            prep.setString(2, email);
            ResultSet rs = prep.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getLearnerIDByEmail(String email) {
        String learnerID = "";
        try {
            Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
            PreparedStatement prep = conn.prepareStatement("SELECT UserID FROM tblUser WHERE UserRole = 'Learner' AND UserEmail = ?");
            prep.setString(1, email);
            ResultSet rs = prep.executeQuery();
            if (rs.next()) {
                learnerID = rs.getString("UserID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return learnerID;
    }

    public Learner findLearnerByEmail(String email) {
        Learner learner = null;
        try {
            Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
            PreparedStatement prep = conn.prepareStatement("SELECT * FROM tblUser WHERE UserRole = ? AND UserEmail = ?");
            prep.setString(1, "Learner");
            prep.setString(2, email);
            ResultSet rs = prep.executeQuery();
            if (rs.next()) {
                learner = new Learner();
                learner.setId(rs.getString("UserID"));
                learner.setLearnerFirstName(rs.getString("UserFirstName"));
                learner.setLastName(rs.getString("UserLastName"));
                learner.setEmail(rs.getString("UserEmail"));
                learner.setLearnerPhoneNumber(rs.getString("UserPhone"));
                learner.setLearnerDob(rs.getDate("UserDob"));
                learner.setLearnerAge(rs.getInt("UserAge"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return learner;
    }

    public String getLearnerFirstName(String email) {
        String learnerFirstName = "";
        try {
            Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
            PreparedStatement prep = conn.prepareStatement("SELECT UserFirstName FROM tblUser WHERE UserRole = 'Learner' AND UserEmail = ?");
            prep.setString(1, email);
            ResultSet rs = prep.executeQuery();
            if (rs.next()) {
                learnerFirstName = rs.getString("UserFirstName");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return learnerFirstName;
    }

    private final ArrayList<String> learnerList = new ArrayList<>();

    public ArrayList<String> getLearnerList() {
        learnerList.clear();
        try {
            Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
            PreparedStatement prep = conn.prepareStatement("SELECT * FROM tblUser WHERE UserRole = 'Learner'");
            ResultSet rs = prep.executeQuery();
            while (rs.next()) {
                learnerList.add(rs.getString("UserFirstName"));
                learnerList.add(rs.getString("UserLastName"));
                learnerList.add(rs.getString("UserEmail"));
                learnerList.add(rs.getString("UserPhone"));
                learnerList.add(rs.getString("UserAge"));
                learnerList.add(rs.getString("UserDOB"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return learnerList;
    }
}
