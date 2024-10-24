package service;

import model.JDBC;
import model.Learner;
import repository.LearnerRepo;
import utils.PasswordEncryption;
import utils.Utils;

import java.lang.reflect.Field;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class LearnerService {
    Scanner scanner = new Scanner(System.in);
    LearnerRepo learnerRepo = new LearnerRepo();

    public void add() {
        String password = PasswordEncryption.hashPassword((Utils.getProperPassword("Enter password: ")));
        String firstName = Utils.getProperName("Enter first name: ");
        String lastName = Utils.getProperName("Enter last name: ");
        String email = Utils.getValidEmail("Enter email: ");
        String phoneNumber = Utils.getPhoneNumber("Enter phone number: ");
        LocalDate dob = LocalDate.parse(Utils.getString("Enter DOB (dd/MM/yyyy): ", scanner), DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        String confirm = Utils.getString("Is this information correct? (Y/N): ", scanner);
        if (confirm.equalsIgnoreCase("Y")) {
            try {
                Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
                PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO tblLearner(LearnerID, LearnerFirstName, LearnerLastName, LearnerEmail, LearnerPhone, LearnerDob, Password) VALUES (?, ?, ?, ?, ?, ?, ?)");
                preparedStatement.setString(1, autoGenerateLearnerID(conn));
                preparedStatement.setString(2, firstName);
                preparedStatement.setString(3, lastName);
                preparedStatement.setString(4, email);
                preparedStatement.setString(5, phoneNumber);
                preparedStatement.setDate(6, Date.valueOf(dob));
                preparedStatement.setString(7, password);
                preparedStatement.executeUpdate();
                System.out.println("Learner added");
            } catch (SQLException e) {
                System.err.println("SQL Exception: " + e.getMessage());
            }
        } else {
            System.out.println("Learner not added");
        }
    }

    public void viewProfile(String learnerId) {
        System.out.println("Learner found:");
        Learner learner = learnerRepo.findLearnerById(learnerId);
        System.out.println(learner);
    }

    public void update(String id) {
        try {
            System.out.println("Learner found:");
            Learner learner = learnerRepo.findLearnerById(id);
            System.out.println(learner);

            Class<?> learnerFields = Class.forName("model.Learner");

            Field[] fields = learnerFields.getDeclaredFields();
            for (Field field : fields) {
                System.out.print(field.getName() + "\t");
            }

            String attribute = Utils.getString("\nEnter attribute to update: ", scanner);
            String newValue = Utils.getString("Enter new value: ", scanner);

            try {
                Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
                PreparedStatement preparedStatement = conn.prepareStatement("UPDATE tblLearner SET " + attribute + " = ? WHERE LearnerID = ?");
                preparedStatement.setString(1, newValue);
                preparedStatement.setString(2, id);
                preparedStatement.executeUpdate();
                System.out.println("Learner updated");
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

        if (learnerRepo.validateLogin(id, oldPassword)) {
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
        String id = Utils.getString("Enter learner ID: ", scanner);
        if (learnerRepo.checkLearnerIdExist(id)) {
            try {
                String confirm = Utils.getString("Are you sure? (Y/N): ", scanner);
                if (!confirm.equalsIgnoreCase("Y")) {
                    System.out.println("Learner not deleted");
                }
                else {
                    Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
                    PreparedStatement preparedStatement = conn.prepareStatement("DELETE FROM tblLearner WHERE LearnerID = ?");
                    preparedStatement.setString(1, id);
                    preparedStatement.executeUpdate();
                    System.out.println("Learner deleted");
                }
            } catch (SQLException e) {
                System.err.println("SQL Exception: " + e.getMessage());
            }
        }
    }

    public void display() {
        ArrayList<String> learnerList = learnerRepo.getLearnerList();
        System.out.printf("%-10s %-15s %-15s %-25s %-15s %-10s %-10s%n", "LearnerID", "First Name", "Last Name", "Email", "Phone Number", "Age", "Date of Birth");
        System.out.println("--------------------------------------------------------------------------------------");
        for (int i = 0; i < learnerList.size(); i += 7) {
            System.out.printf("%-10s %-15s %-15s %-25s %-15s %-10s %-10s%n", learnerList.get(i), learnerList.get(i + 1), learnerList.get(i + 2), learnerList.get(i + 3), learnerList.get(i + 4), learnerList.get(i + 5), learnerList.get(i + 6));
        }
    }

    private static String autoGenerateLearnerID(Connection connection) {
        String newLearnerId = "L001";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT MAX(LearnerID) AS maxID FROM tblLearner");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String maxID = resultSet.getString("maxID");
                if (maxID != null) {
                    int idNumber = Integer.parseInt(maxID.substring(1)) + 1;
                    newLearnerId = String.format("CH%03d", idNumber);
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }
        return newLearnerId;
    }
}
