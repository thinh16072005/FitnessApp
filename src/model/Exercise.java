package model;

import java.time.Duration;

public class Exercise {
    String exerciseName;
    int sets;
    int reps;
    double caloriesBurn;
    String note;
    Duration duration;
    String workoutId;

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

    public String getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(String workoutId) {
        this.workoutId = workoutId;
    }

    @Override
    public String toString() {
        return String.format("""
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
                "Name", getExerciseName(),
                "sets", getSets(),
                "reps", getReps(),
                "calories", getCaloriesBurn(),
                "Duration", getDuration(),
                "Note", getNote()
        );
                
    }
}