package repository;

import model.Coach;
import model.JDBC;
import model.Learner;

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
            else {
                System.out.println("Learner ID does not exist!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
                learner.setFirstName(rs.getString("LearnerFirstName"));
                learner.setLastName(rs.getString("LearnerLastName"));
                learner.setEmail(rs.getString("LearnerEmail"));
                learner.setPhoneNumber(rs.getString("LearnerPhone"));
                learner.setDob(rs.getDate("LearnerDob"));
                learner.setAge(rs.getInt("LearnerAge"));
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
                learnerList.add(rs.getString("CoachFirstName"));
                learnerList.add(rs.getString("CoachLastName"));
                learnerList.add(rs.getString("CoachEmail"));
                learnerList.add(rs.getString("CoachPhone"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return learnerList;
    }
}
