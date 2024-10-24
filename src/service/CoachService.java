package service;

import model.Coach;
import model.JDBC;
import repository.CoachRepo;
import utils.PasswordEncryption;
import utils.Utils;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class CoachService {
    Scanner scanner = new Scanner(System.in);
    CoachRepo coachRepo = new CoachRepo();

    public void add() {
        String password = PasswordEncryption.hashPassword((Utils.getProperPassword("Enter password: ")));
        String coachFirstName = Utils.getProperName("Enter coach's first name: ");
        String coachLastName = Utils.getProperName("Enter coach's last name: ");
        String coachEmail = Utils.getValidEmail("Enter coach's email: ");
        String coachPhoneNumber = Utils.getPhoneNumber("Enter coach's phone number: ");
        String confirm = Utils.getString("Is this information correct? (Y/N): ", scanner);
        if (confirm.equalsIgnoreCase("Y")) {
            try {
                Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
                PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO tblCoach (CoachID, CoachFirstName, CoachLastName, CoachEmail, CoachPhone, Password) VALUES (?, ?, ?, ?, ?, ?)");
                preparedStatement.setString(1, autoGenerateCoachID(conn));
                preparedStatement.setString(2, coachFirstName);
                preparedStatement.setString(3, coachLastName);
                preparedStatement.setString(4, coachEmail);
                preparedStatement.setString(5, coachPhoneNumber);
                preparedStatement.setString(6, password);
                preparedStatement.executeUpdate();
                System.out.println("Coach added");
            } catch (SQLException e) {
                System.err.println("SQL Exception: " + e.getMessage());
            }
        }
        else {
            System.out.println("Coach not added");
        }
    }

    public void viewProfile(String coachId) {
        System.out.println("Coach found:");
        Coach coach = coachRepo.findCoachById(coachId);
        System.out.println(coach);
    }

    public void update(String id) {
        try {
            System.out.println("Learner found:");
            Coach coach = coachRepo.findCoachById(id);
            System.out.println(coach);

            String attribute = Utils.getString("\nEnter attribute (name of column) to update: ", scanner);
            String newValue = Utils.getString("Enter new value: ", scanner);

            try {
                Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
                PreparedStatement preparedStatement = conn.prepareStatement("UPDATE tblCoach SET " + attribute + " = ? WHERE CoachID = ?");
                preparedStatement.setString(1, newValue);
                preparedStatement.setString(2, id);
                preparedStatement.executeUpdate();
                System.out.println("Coach updated");
            } catch (SQLException e) {
                System.err.println("Exception: " + e.getMessage());
            }
        }
        catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
    }

    public void updatePassword(String id) {
        String oldPassword = PasswordEncryption.hashPassword((Utils.getProperPassword("Enter old password: ")));
        if (coachRepo.validateLogin(id, oldPassword)) {
            String newPassword = PasswordEncryption.hashPassword(Utils.getProperPassword("Enter new password: "));
            try {
                Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
                PreparedStatement preparedStatement = conn.prepareStatement("UPDATE tblLearner SET Password = ? WHERE LearnerID = ?");
                preparedStatement.setString(1, newPassword);
                preparedStatement.setString(2, id);
                preparedStatement.executeUpdate();
                System.out.println("Password updated");
            } catch (SQLException e) {
                System.err.println("SQL Exception: " + e.getMessage());
            }
        } else {
            System.out.println("Old password is incorrect");
        }
    }

    public void delete() {
        String coachId = Utils.getString("Enter course ID: ", scanner);
        if (!coachRepo.checkCoachIdExist(coachId)) {
            System.out.println("Course ID does not exist!");
        } else {
            String confirm = Utils.getString("Are you sure? (Y/N): ", scanner);
            if (!confirm.equalsIgnoreCase("Y")) {
                System.out.println("Course not deleted");
            } else {
                try {
                    Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
                    PreparedStatement prep = conn.prepareStatement("DELETE FROM tblCoach WHERE CoachID = ?");
                    prep.setString(1, coachId);
                    prep.executeUpdate();
                    System.out.println("Course deleted");
                } catch (SQLException e) {
                    System.err.println("SQL Exception: " + e.getMessage());
                }
            }
        }
    }

    public void display() {
        ArrayList<String> coachList = coachRepo.getCoachList();
        System.out.printf("%-10s %-15s %-15s %-25s %-15s%n", "CoachID", "First Name", "Last Name", "Email", "Phone Number");
        System.out.println("--------------------------------------------------------------------------------------");
        for (int i = 0; i < coachList.size(); i += 5) {
            System.out.printf("%-10s %-15s %-15s %-25s %-15s%n", coachList.get(i), coachList.get(i + 1), coachList.get(i + 2), coachList.get(i + 3), coachList.get(i + 4));
        }
    }

    private static String autoGenerateCoachID(Connection connection) {
        String newCoachId = "CH001";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT MAX(CoachID) AS maxID FROM tblCoach");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String maxID = resultSet.getString("maxID");
                if (maxID != null) {
                    int idNumber = Integer.parseInt(maxID.substring(2)) + 1;
                    newCoachId = String.format("CH%03d", idNumber);
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }
        return newCoachId;
    }
}
