package service;

import model.JDBC;
import model.Learner;
import repository.LearnerRepo;
import utils.Utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class LearnerService {
    Scanner scanner = new Scanner(System.in);
    LearnerRepo learnerRepo = new LearnerRepo();

    public void add() {
        String firstName = Utils.getProperName("Enter first name: ");
        String lastName = Utils.getProperName("Enter last name: ");
        String email = Utils.getValidEmail("Enter email: ");
        String phoneNumber = Utils.getPhoneNumber("Enter phone number: ");
        String dob = Utils.getValidDate("Enter date of birth (dd/MM/yyyy): ");
        int age = Utils.calculateAge(dob);
        boolean confirm = Utils.getBoolean("Is this information correct? (Y/N): ", scanner);
        if (confirm) {
            try {
                Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
                PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO tblLearner VALUES (?, ?, ?, ?, ?, ?, ?)");
                preparedStatement.setString(1, autoGenerateLearnerID(conn));
                preparedStatement.setString(2, firstName);
                preparedStatement.setString(3, lastName);
                preparedStatement.setString(4, email);
                preparedStatement.setString(5, phoneNumber);
                preparedStatement.setString(6, dob);
                preparedStatement.setInt(7, age);
                preparedStatement.executeUpdate();
                System.out.println("Learner added");
            } catch (SQLException e) {
                System.err.println("SQL Exception: " + e.getMessage());
            }
            System.out.println("Learner added");
        } else {
            System.out.println("Learner not added");
        }
    }

    public void update(Learner learner) throws ClassNotFoundException {
        String id = Utils.getString("Enter learner ID: ", scanner);

    }

    public void delete(Learner learner) {
        String id = Utils.getString("Enter learner ID: ", scanner);
        if (learnerRepo.checkLearnerIdExist(id)) {
            try {
                Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
                PreparedStatement preparedStatement = conn.prepareStatement("DELETE FROM tblLearner WHERE LearnerID = ?");
                preparedStatement.setString(1, id);
                preparedStatement.executeUpdate();
                System.out.println("Learner deleted");
            } catch (SQLException e) {
                System.err.println("SQL Exception: " + e.getMessage());
            }
        }
    }

    public void display() {
        ArrayList<String> learnerList = learnerRepo.getLearnerList();
        System.out.printf("%-10s %-15s %-15s %-25s %-15s %-10s %-10s%n", "CoachID", "First Name", "Last Name", "Email", "Phone Number", "Date of Birth", "Age");
        System.out.println("--------------------------------------------------------------------------------------");
        for (int i = 0; i < learnerList.size(); i += 5) {
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
