package view;

import model.JDBC;
import utils.Utils;

import java.sql.*;
import java.util.Scanner;

public class LearnerMenu {
    public static String[] learnerOptions = {"View course", "Register course", "Delete course"};
    Scanner input = new Scanner(System.in);

    private boolean checkLearnerIdExist() {
        String learnerId = Utils.getString("Enter Learner ID", input);
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

    public static void displayLearnerMenu() throws ClassNotFoundException {
//        checkLearnerIdExist();
        Menu coachMenu = new Menu("\nHELLO, COACH", learnerOptions) {
            @Override
            public void execute(int ch) {
                switch (ch) {

                }
            }
        };
        coachMenu.run();
    }
}
