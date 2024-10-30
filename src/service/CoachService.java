package service;

import model.Coach;
import model.JDBC;
import repository.CoachRepo;
import repository.UserRepo;
import utils.PasswordEncryption;
import utils.Utils;

import java.lang.reflect.Field;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class CoachService {
    Scanner scanner = new Scanner(System.in);
    CoachRepo coachRepo = new CoachRepo();
    UserRepo userValidation = new UserRepo();

    public void add() {
        String coachFirstName = Utils.getProperName("Enter coach's first name: ");
        String coachLastName = Utils.getProperName("Enter coach's last name: ");
        String coachEmail = Utils.getValidEmail("Enter coach's email: ");
        String coachPhoneNumber = Utils.getPhoneNumber("Enter coach's phone number: ");
        LocalDate dob = LocalDate.parse(Utils.getString("Enter DOB (dd/MM/yyyy): ", scanner), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String password, rePassword;
        while (true) {
            password = PasswordEncryption.hashPassword(Utils.getProperPassword("Enter password: "));
            rePassword = PasswordEncryption.hashPassword(Utils.getProperPassword("Re-enter password: "));
            if (password.equals(rePassword)) {
                break;
            } else {
                System.out.println("Passwords do not match. Please try again.");
            }
        }
        String confirm = Utils.getString("Is this information correct? (Y/N): ", scanner);
        if (confirm.equalsIgnoreCase("Y")) {
            try {
                Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
                PreparedStatement preparedStatement = conn.prepareStatement("EXEC proc_AddUser " +
                        "@UserID = ?, " +
                        "@UserRole = ?, " +
                        "@UserFirstName = ?, " +
                        "@UserLastName = ?, " +
                        "@UserEmail = ?, " +
                        "@UserPhone = ?, " +
                        "@UserDOB = ?, " +
                        "@Password = ?");
                preparedStatement.setString(1, autoGenerateCoachID(conn));
                preparedStatement.setString(2, "Coach");
                preparedStatement.setString(3, coachFirstName);
                preparedStatement.setString(4, coachLastName);
                preparedStatement.setString(5, coachEmail);
                preparedStatement.setString(6, coachPhoneNumber);
                preparedStatement.setDate(7, Date.valueOf(dob));
                preparedStatement.setString(8, password);
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

    public void viewProfile(String email) {
        System.out.println("Coach found:");
        Coach coach = coachRepo.findCoachById(email);
        System.out.println(coach);
    }

    public void update(String email, String userRole) throws ClassNotFoundException {
        viewProfile(email);
        Class<?> coachFields = Class.forName("model.User");
        Field[] fields = coachFields.getDeclaredFields();
        for (Field field : fields) {
            System.out.print(field.getName() + "\t");
        }
        try {
            System.out.println("Coach found:");
            Coach coach = coachRepo.findCoachById(email);
            System.out.println(coach);

            String attribute = Utils.getString("\nEnter attribute (name of column) to update: ", scanner);
            String newValue = Utils.getString("Enter new value: ", scanner);
            try {
                Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
                String query = "UPDATE tblUser SET " + attribute + " = ? WHERE UserEmail = ? AND UserRole = ?";
                PreparedStatement preparedStatement = conn.prepareStatement(query);
                if ("UserDOB".equalsIgnoreCase(attribute)) {
                    LocalDate dob = LocalDate.parse(newValue, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    preparedStatement.setDate(1, Date.valueOf(dob));
                } else {
                    preparedStatement.setString(1, newValue);
                }
                preparedStatement.setString(2, email);
                preparedStatement.setString(3, userRole);
                preparedStatement.executeUpdate();
                System.out.println("User updated");
            } catch (SQLException e) {
                System.err.println("Exception: " + e.getMessage());
            }
        }
        catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
    }

    public void updatePassword(String email) {
        String oldPassword = PasswordEncryption.hashPassword((Utils.getProperPassword("Enter old password: ")));
        if (userValidation.validatePassword(email, oldPassword)) {
            String newPassword = PasswordEncryption.hashPassword(Utils.getProperPassword("Enter new password: "));
            try {
                Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
                PreparedStatement preparedStatement = conn.prepareStatement("UPDATE tblUser SET Password = ? WHERE UserRole = ? AND UserEmail = ?");
                preparedStatement.setString(1, newPassword);
                preparedStatement.setString(2, "Coach");
                preparedStatement.setString(3, email);
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
        String coachId = Utils.getString("Enter coach ID: ", scanner);
        Coach coach = coachRepo.findCoachById(coachId);
        if (!coachRepo.checkCoachIdExist(coachId)) {
            System.out.println("Course ID does not exist!");
        } else {
            System.out.println(coach);
            String confirm = Utils.getString("Are you sure? (Y/N): ", scanner);
            if (!confirm.equalsIgnoreCase("Y")) {
                System.out.println("Course not deleted");
            } else {
                try {
                    Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
                    PreparedStatement prep = conn.prepareStatement("DELETE FROM tblCoach WHERE CoachID = ?");
                    prep.setString(1, coachId);
                    prep.executeUpdate();
                    System.out.println("Coach deleted");
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
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT MAX(UserID) AS maxID FROM tblUser WHERE UserRole = 'Coach'");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String maxID = resultSet.getString("maxID");
                if (maxID != null) {
                    int idNumber = Integer.parseInt(maxID.trim().substring(2)) + 1;
                    newCoachId = String.format("CH%03d", idNumber);
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }
        return newCoachId;
    }
}
