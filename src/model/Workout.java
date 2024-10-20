package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Workout {
    private String workoutID;
    private String workoutName;
    private String exercise;
    private String nutrition;
    private double bmi;
    private double calories;
    private String coachID;

    public Workout(String workoutID, String workoutName, String exercise, String nutrition, double bmi, double calories, String coachID) {
        this.workoutID = workoutID;
        this.workoutName = workoutName;
        this.exercise = exercise;
        this.nutrition = nutrition;
        this.bmi = bmi;
        this.calories = calories;
        this.coachID = coachID;
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

    public String getCoachID() {
        return coachID;
    }

    public void setCoachID(String coachID) {
        this.coachID = coachID;
    }
}
