package repository;

import model.JDBC;
import utils.PasswordEncryption;
import utils.Utils;

import java.sql.*;
import java.util.Scanner;

public class AdminRepo {
    public boolean validateLogin(String username, String password) {
        try {
            String hashedPassword = PasswordEncryption.hashPassword(password);
            Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
            PreparedStatement prep = conn.prepareStatement("SELECT COUNT(*) FROM tblAdmin WHERE Username = ? AND Password = ?");
            prep.setString(1, username);
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

    public void createAdmin() {
        Scanner input = new Scanner(System.in);
        String username = Utils.getString("Enter username: ", input);
        String password = Utils.getProperPassword("Enter password: ");
        try {
            Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
            PreparedStatement prep = conn.prepareStatement("INSERT INTO tblAdmin (Username, Password) VALUES (?, ?)");
            prep.setString(1, username);
            prep.setString(2, PasswordEncryption.hashPassword(password));
            prep.executeUpdate();
            System.out.println("Admin created");
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }
    }
}
