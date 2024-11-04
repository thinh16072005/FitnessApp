package view;

import repository.CoachRepo;
import service.CourseService;
import service.ExerciseService;

public class CoachSubMenus {

    public void displayCourseMenu(String email) throws Exception {
        CourseService courseService = new CourseService();
        CoachRepo coachRepo = new CoachRepo();
        String[] courseOptions = {
                "View course",
                "Create course",
                "Update course",
                "Delete course",
                "Display learners of your course",
                "Exit"
        };
        Menu courseMenu = new Menu("COURSE MANAGEMENT", courseOptions) {
            @Override
            public void execute(int ch) throws ClassNotFoundException {
                switch (ch) {
                    case 1 -> courseService.display();
                    case 2 -> courseService.add(email);
                    case 3 -> courseService.update();
                    case 4 -> courseService.delete();
                    case 5 -> courseService.displayLearners(coachRepo.getCoachIDByEmail(email));
                    case 6 -> System.out.println("Exiting...");
                    default -> System.out.println("Invalid option");
                }
            }
        };
        courseMenu.run();
    }

//    public void displayWorkoutMenu() throws Exception {
//        WorkoutService workoutService = new WorkoutService();
//
//        String[] workoutOptions = {
//                "View workout",
//                "Create workout",
//                "Update workout",
//                "Delete workout",
//                "Exit"
//        };
//        Menu workoutMenu = new Menu("WORKOUT MANAGEMENT", workoutOptions) {
//            @Override
//            public void execute(int ch) throws ClassNotFoundException {
//                switch (ch) {
//                    case 1 -> workoutService.display();
//                    case 2 -> workoutService.add();
//                    case 3 -> workoutService.update();
//                    case 4 -> workoutService.delete();
//                }
//            }
//        };
//        workoutMenu.run();
//    }

    public void displayExerciseMenu() throws Exception {
        ExerciseService exerciseService = new ExerciseService();

        String[] exerciseOptions = {
                "View exercise",
                "Create exercise",
                "Add exercise to course",
                "Update exercise",
                "Delete exercise",
                "Exit"
        };
        Menu exerciseMenu = new Menu("EXERCISE MANAGEMENT", exerciseOptions) {
            @Override
            public void execute(int ch) {
                switch (ch) {
                    case 1 -> exerciseService.display();
                    case 2 -> exerciseService.add();
                    case 3 -> exerciseService.ExerciseToCourse();
                    case 4 -> exerciseService.update();
                    case 5 -> exerciseService.delete();
                    case 6 -> System.out.println("Exiting...");
                    default -> System.out.println("Invalid option");
                }
            }
        };
        exerciseMenu.run();
    }
}
