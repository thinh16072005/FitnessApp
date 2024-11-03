package service;

import model.JDBC;
import model.Learner;
import repository.LearnerRepo;
import repository.UserRepo;
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
    UserRepo userRepo = new UserRepo();
    LearnerRepo learnerRepo = new LearnerRepo();

    public void add() {
        String firstName = Utils.getProperName("Enter first name: ");
        String lastName = Utils.getProperName("Enter last name: ");
        String email = Utils.getValidEmail("Enter email: ");
        String phoneNumber = Utils.getPhoneNumber("Enter phone number: ");
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
                        "@Password = ?"
                );
                preparedStatement.setString(1, autoGenerateLearnerID(conn));
                preparedStatement.setString(2, "Learner");
                preparedStatement.setString(3, firstName);
                preparedStatement.setString(4, lastName);
                preparedStatement.setString(5, email);
                preparedStatement.setString(6, phoneNumber);
                preparedStatement.setDate(7, Date.valueOf(dob));
                preparedStatement.setString(8, password);
                preparedStatement.executeUpdate();
                System.out.println("Learner added");
            } catch (SQLException e) {
                System.err.println("SQL Exception: " + e.getMessage());
            }
        } else {
            System.out.println("Learner not added");
        }

    }

    public void viewProfile(String email) {
        Learner learner = learnerRepo.findLearnerByEmail(email);
        System.out.println(learner);
    }

    public void update(String email, String userRole) throws ClassNotFoundException {
        viewProfile(email);

        Class<?> learnerFields = Class.forName("model.User");
        Field[] fields = learnerFields.getDeclaredFields();
        for (Field field : fields) {
            System.out.print(field.getName() + "\t");
        }

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
            System.err.println("SQL Exception: " + e.getMessage());
        }
    }

    public void updatePassword(String email) {
        String oldPassword = PasswordEncryption.hashPassword((Utils.getProperPassword("Enter old password: ")));
        if (userRepo.validatePassword(email, oldPassword)) {
            String newPassword = PasswordEncryption.hashPassword(Utils.getProperPassword("Enter new password: "));
            try {
                Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
                PreparedStatement preparedStatement = conn.prepareStatement("UPDATE tblUser SET Password = ? WHERE UserEmail = ? AND UserRole = ?");
                preparedStatement.setString(1, newPassword);
                preparedStatement.setString(2, email);
                preparedStatement.setString(3, "Learner");
                preparedStatement.executeUpdate();
                System.out.println("Password updated.");
            } catch (SQLException e) {
                System.err.println("SQL Exception: " + e.getMessage());
            }
        } else {
            System.err.println("Old password is incorrect");
        }
    }

    public void delete() {
        String id = Utils.getString("Enter learner ID: ", scanner);
        Learner learner = learnerRepo.findLearnerByEmail(id);
        if (learnerRepo.checkLearnerExist(id)) {
            try {
                System.out.println(learner);
                String confirm = Utils.getString("Are you sure? (Y/N): ", scanner);
                if (!confirm.equalsIgnoreCase("Y")) {
                    System.out.println("Learner not deleted");
                }
                else {
                    Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
                    PreparedStatement preparedStatement = conn.prepareStatement("DELETE FROM tblUser WHERE UserRole = ? AND UserEmail = ?");
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
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT MAX(UserID) AS maxID FROM tblUser WHERE UserRole = 'Learner'");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String maxID = resultSet.getString("maxID");
                if (maxID != null) {
                    int idNumber = Integer.parseInt(maxID.trim().substring(1)) + 1;
                    newLearnerId = String.format("L%03d", idNumber);
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }
        return newLearnerId;
    }
}
