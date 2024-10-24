package repository;

import model.JDBC;
import model.Learner;
import utils.PasswordEncryption;

import java.sql.*;
import java.util.ArrayList;

public class LearnerRepo {
    public boolean checkLearnerIdExist(String learnerId) {
        try {
            Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
            PreparedStatement prep = conn.prepareStatement("SELECT COUNT(*) FROM tblLearner WHERE LearnerID = ?");
            prep.setString(1, learnerId);
            ResultSet rs = prep.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean validateLogin(String learnerId, String password) {
        try {
            String hashedPassword = PasswordEncryption.hashPassword(password);
            Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
            PreparedStatement prep = conn.prepareStatement("SELECT COUNT(*) FROM tblLearner WHERE LearnerID = ? AND Password = ?");
            prep.setString(1, learnerId);
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

    public Learner findLearnerById(String learnerId) {
        Learner learner = null;
        try {
            Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
            PreparedStatement prep = conn.prepareStatement("SELECT * FROM tblLearner WHERE LearnerID = ?");
            prep.setString(1, learnerId);
            ResultSet rs = prep.executeQuery();
            if (rs.next()) {
                learner = new Learner();
                learner.setId(rs.getString("LearnerID"));
                learner.setLearnerFirstName(rs.getString("LearnerFirstName"));
                learner.setLastName(rs.getString("LearnerLastName"));
                learner.setEmail(rs.getString("LearnerEmail"));
                learner.setLearnerPhoneNumber(rs.getString("LearnerPhone"));
                learner.setLearnerDob(rs.getDate("LearnerDob"));
                learner.setLearnerAge(rs.getInt("LearnerAge"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return learner;
    }

    private final ArrayList<String> learnerList = new ArrayList<>();

    public ArrayList<String> getLearnerList() {
        learnerList.clear();
        try {
            Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
            PreparedStatement prep = conn.prepareStatement("SELECT * FROM tblLearner");
            ResultSet rs = prep.executeQuery();
            while (rs.next()) {
                learnerList.add(rs.getString("LearnerID"));
                learnerList.add(rs.getString("LearnerFirstName"));
                learnerList.add(rs.getString("LearnerLastName"));
                learnerList.add(rs.getString("LearnerEmail"));
                learnerList.add(rs.getString("LearnerPhone"));
                learnerList.add(rs.getString("LearnerAge"));
                learnerList.add(rs.getString("LearnerDob"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return learnerList;
    }
}
