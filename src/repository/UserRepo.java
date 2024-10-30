package repository;

import model.JDBC;
import utils.PasswordEncryption;

import java.sql.*;

public class UserRepo {
    public boolean validateLoginByEmail(String email, String password, String userType) {
        String userRole = userType.equalsIgnoreCase("learner") ? "Learner" : "Coach";
        try {
            String hashedPassword = PasswordEncryption.hashPassword(password);
            Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
            PreparedStatement prep = conn.prepareStatement("SELECT COUNT(*) FROM tblUser WHERE UserRole = ? AND UserEmail = ? AND Password = ?");
            prep.setString(1, userRole);
            prep.setString(2, email);
            prep.setString(3, hashedPassword);
            ResultSet rs = prep.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean validatePassword(String email, String password) {
        try {
            Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
            PreparedStatement prep = conn.prepareStatement("SELECT COUNT(*) FROM tblUser WHERE UserEmail = ? AND Password = ?");
            prep.setString(1, email);
            prep.setString(2, password);
            ResultSet rs = prep.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }
        return false;
    }
}
