package view;

import service.CourseService;
import service.ExerciseService;
import service.WorkoutService;

public class CoachSubMenus {

    public void displayCourseMenu(String id) throws ClassNotFoundException {
        CourseService courseService = new CourseService();
        String[] courseOptions = {
                "View course",
                "Create course",
                "Update course",
                "Delete course",
                "Exit"
        };
        Menu<String> courseMenu = new Menu<>("COURSE MANAGEMENT", courseOptions) {
            @Override
            public void execute(int ch) throws ClassNotFoundException {
                switch (ch) {
                    case 1 -> courseService.display();
                    case 2 -> courseService.add(id);
                    case 3 -> courseService.update();
                    case 4 -> courseService.delete();
                }
            }
        };
        courseMenu.run();
    }

    public void displayWorkoutMenu() throws ClassNotFoundException {
        WorkoutService workoutService = new WorkoutService();

        String[] workoutOptions = {
                "View workout",
                "Create workout",
                "Update workout",
                "Delete workout",
                "Exit"
        };
        Menu<String> workoutMenu = new Menu<>("WORKOUT MANAGEMENT", workoutOptions) {
            @Override
            public void execute(int ch) throws ClassNotFoundException {
                switch (ch) {
                    case 1 -> workoutService.display();
                    case 2 -> workoutService.add();
                    case 3 -> workoutService.update();
                    case 4 -> workoutService.delete();
                }
            }
        };
        workoutMenu.run();
    }

    public void displayExerciseMenu() throws ClassNotFoundException {
        ExerciseService exerciseService = new ExerciseService();

        String[] exerciseOptions = {
                "View exercise",
                "Create exercise",
                "Update exercise",
                "Delete exercise",
                "Exit"
        };
        Menu<String> exerciseMenu = new Menu<>("EXERCISE MANAGEMENT", exerciseOptions) {
            @Override
            public void execute(int ch) {
                switch (ch) {
                    case 1 -> exerciseService.display();
                    case 2 -> exerciseService.add();
                    case 3 -> exerciseService.update();
                    case 4 -> exerciseService.delete();
                }
            }
        };
        exerciseMenu.run();
    }
}
