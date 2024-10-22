package model;

public class Workout {
    private String workoutID;
    private String workoutName;
    private String exercise;
    private String nutrition;
    private double bmi;
    private double calories;
    private String courseId;

    public Workout(String workoutID, String workoutName, String exercise, String nutrition, double bmi, double calories, String courseId) {
        this.workoutID = workoutID;
        this.workoutName = workoutName;
        this.exercise = exercise;
        this.nutrition = nutrition;
        this.bmi = bmi;
        this.calories = calories;
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

    public String getExercise() {
        return exercise;
    }

    public void setExercise(String exercise) {
        this.exercise = exercise;
    }

    public String getNutrition() {
        return nutrition;
    }

    public void setNutrition(String nutrition) {
        this.nutrition = nutrition;
    }

    public double getBmi() {
        return bmi;
    }

    public void setBmi(double bmi) {
        this.bmi = bmi;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
}
