package repository;

import model.Course;
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

    // src/repository/CourseRepo.java
    public Course findCourseByEmail(String courseId) {
        Course course = null;
        try {
            Connection conn = DriverManager.getConnection(JDBC.DB_URL, JDBC.DB_USERNAME, JDBC.DB_PASSWORD);
            PreparedStatement prep = conn.prepareStatement("SELECT CourseID, CourseName, CourseDescription, CourseDuration, StartDate, EndDate, CoachID FROM tblCourse WHERE CourseID = ?");
            prep.setString(1, courseId);
            ResultSet rs = prep.executeQuery();
            if (rs.next()) {
                course = new Course();
                course.setCourseID(rs.getString("CourseID"));
                course.setCourseName(rs.getString("CourseName"));
                course.setDescription(rs.getString("CourseDescription"));
                course.setDuration(rs.getInt("CourseDuration"));
                course.setStartDate(rs.getDate("StartDate"));
                course.setEndDate(rs.getDate("EndDate"));
                course.setCoachID(rs.getString("CoachID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return course;
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
                courseList.add(rs.getString("UserID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courseList;
    }
}
