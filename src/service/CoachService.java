package service;

import model.Coach;
import model.JDBC;
import repository.CoachRepo;
import utils.Utils;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class CoachService {
    Scanner scanner = new Scanner(System.in);
    CoachRepo coachRepo = new CoachRepo();

    public void add(Coach coach) {
        String coachFirstName = Utils.getProperName("Enter coach's first name: ");
        String coachLastName = Utils.getProperName("Enter coach's last name: ");
        String coachEmail = Utils.getValidEmail("Enter coach's email: ");
        String coachPhoneNumber = Utils.getPhoneNumber("Enter coach's phone number: ");
        try {
            Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
            PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO tblCoach VALUES (?, ?, ?, ?, ?)");
            preparedStatement.setString(1, autoGenerateCoachID(conn));
            preparedStatement.setString(2, coachFirstName);
            preparedStatement.setString(3, coachLastName);
            preparedStatement.setString(4, coachEmail);
            preparedStatement.setString(5, coachPhoneNumber);
            preparedStatement.executeUpdate();
            System.out.println("Coach added");
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }
    }

    public void update(Coach coach) throws ClassNotFoundException {
        String id = Utils.getString("Enter coach ID: ", scanner);
        try {
            if (!coachRepo.checkCoachIdExist(id)) {
                System.out.println("Coach ID does not exist!");
                return;
            }
            System.out.println("Coach found:");
            coach = coachRepo.findCoachById(id);
            System.out.println(coach);

            Class<?> customerFields = Class.forName("model.Coach");

            Field[] fields = customerFields.getSuperclass().getDeclaredFields();
            for (Field field : fields) {
                System.out.print("\t" + field.getName());
            }

            String attribute = Utils.getString("\nEnter attribute to update: ", scanner);
            switch (attribute.toLowerCase()) {
                case "firstname" -> coach.setCoachFirstName(Utils.getProperName("Enter new first name: "));
                case "lastname" -> coach.setCoachLastName(Utils.getProperName("Enter new last name: "));
                case "email" -> coach.setCoachEmail(Utils.getValidEmail("Enter new email: "));
                case "phonenumber" -> coach.setCoachPhoneNumber(Utils.getPhoneNumber("Enter new phone number: "));
                default -> {
                    System.out.println("Invalid attribute.");
                    return;
                }
            }
        } catch (Exception ex) {
            System.out.println("Error updating coach: " + ex.getMessage());
        }
    }

    public void delete(Coach coach) {
        String id = Utils.getString("Enter coach ID: ", scanner);
        if (coachRepo.checkCoachIdExist(id)) {
            try {
                Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
                PreparedStatement preparedStatement = conn.prepareStatement("DELETE FROM tblCoach WHERE CoachID = ?");
                preparedStatement.setString(1, id);
                preparedStatement.executeUpdate();
                System.out.println("Coach deleted");
            } catch (SQLException e) {
                System.err.println("SQL Exception: " + e.getMessage());
            }
        }
        else {
            System.out.println("Coach ID does not exist!");
        }
    }

    public void display() {
        ArrayList<String> coachList = coachRepo.getCoachList();
//        coachList.clear();
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
