package model;

import java.time.Duration;

public class Exercise {
    String exerciseName;
    int sets;
    int reps;
    double caloriesBurn;
    String note;
    Duration duration;

    public Exercise() {}

    public Exercise(String exerciseName, int sets, int reps, double caloriesBurn, Duration duration, String note) {
        this.exerciseName = exerciseName;
        this.sets = sets;
        this.reps = reps;
        this.caloriesBurn = caloriesBurn;
        this.duration = duration;
        this.note = note;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public double getCaloriesBurn() {
        return caloriesBurn;
    }

    public void setCaloriesBurn(double caloriesBurn) {
        this.caloriesBurn = caloriesBurn;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return String.format("""
                        | %-15s | %-20s |
                        +-----------------+----------------------+
                        | %-15s | %-20s |
                        +-----------------+----------------------+
                        | %-15s | %-20s |
                        | %-15s | %-20s |
                        | %-15s | %-20s |
                        | %-15s | %-20s |
                        | %-15s | %-20s |
                        +-----------------+----------------------+
                        """,
                "Field", "Value",
                "Exercise name", getExerciseName(),
                "Number of sets", getSets(),
                "Number of reps", getReps(),
                "Number of calories", getCaloriesBurn(),
                "Duration of exercise", getDuration(),
                "Note for excercise", getNote()
        );
                
    }
}