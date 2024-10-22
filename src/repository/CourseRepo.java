package repository;

import model.JDBC;

import java.sql.*;
import java.util.ArrayList;

public class CourseRepo {
    private final ArrayList<String> courseList = new ArrayList<>();

    public boolean checkCourseIdExist(String courseId) {
        try {
            Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
            PreparedStatement prep = conn.prepareStatement("SELECT COUNT(*) FROM tblCourse WHERE CourseID = ?");
            prep.setString(1, courseId);
            ResultSet rs = prep.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            else {
                System.err.println("Course ID does not exist!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<String> getCourseList() {
        courseList.clear();
        try {
            Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
            PreparedStatement prep = conn.prepareStatement("SELECT * FROM tblCourse");
            ResultSet rs = prep.executeQuery();
            while (rs.next()) {
                courseList.add(rs.getString("CourseID"));
                courseList.add(rs.getString("CourseName"));
                courseList.add(rs.getString("CourseDescription"));
                courseList.add(rs.getString("CourseDuration"));
                courseList.add(rs.getString("StartDate"));
                courseList.add(rs.getString("EndDate"));
                courseList.add(rs.getString("CoachID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courseList;
    }
}
