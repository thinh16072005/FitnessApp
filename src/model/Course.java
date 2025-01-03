package model;

import java.util.Date;

public class Course {
    String courseID;
    String courseName;
    String description;
    int duration;
    Date startDate;
    Date endDate;
    String coachID;

    public Course() {}

    public Course(String courseID, String courseName, String description, int duration, Date startDate, Date endDate) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.description = description;
        this.duration = duration;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//    public ArrayList<Workout> getWorkout() {
//        return workout;
//    }
//
//    public void setWorkout(Workout workout) {
//        this.workout = workout;
//    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getCoachID() {
        return coachID;
    }

    public void setCoachID(String coachID) {
        this.coachID = coachID;
    }


    @Override
    public String toString() {
        return String.format("""
                         +-----------------+----------------------+
                         | %-15s | %-20s |
                         +-----------------+----------------------+
                         | %-15s | %-20s |
                         | %-15s | %-20s |
                         | %-15s | %-20d |
                         | %-15s | %-20s |
                         | %-15s | %-20s |
                         +-----------------+----------------------+
                         """,
                "Course ID", courseID,
                "Name", courseName,
                "Description", description,
                "Duration", duration,
                "Start", startDate,
                "End", endDate
        );
    }
}