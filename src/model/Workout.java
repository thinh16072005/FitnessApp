package model;

public class Workout {
    private String workoutID;
    private String workoutName;
    private String courseId;

    public Workout() {}

    public Workout(String workoutID, String workoutName, String courseId) {
        this.workoutID = workoutID;
        this.workoutName = workoutName;
        this.courseId = courseId;
    }

    public String getWorkoutID() {
        return workoutID;
    }

    public void setWorkoutID(String workoutID) {
        this.workoutID = workoutID;
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
}
