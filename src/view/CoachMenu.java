package view;

import repository.CoachRepo;
import service.CourseService;
import service.WorkoutService;
import utils.Utils;

import java.util.Scanner;

public class CoachMenu {
    public static String[] coachOptions = {
            "Add new course", "Update course", "Delete course", "Display courses", "Display learners of a course",
            "Add workout to course", "Update workout", "Delete workout", "Display workouts", "Exit"};
    static Scanner input = new Scanner(System.in);

    public static void displayCoachMenu() throws ClassNotFoundException {
        CourseService courseService = new CourseService();
        CoachRepo coachRepo = new CoachRepo();
        WorkoutService workoutService = new WorkoutService();

        String coachId = Utils.getString("Enter coach ID: ", input);
        String password = Utils.getPassword("Enter password: ");

        if (!coachRepo.validateLogin(coachId, password)) {
            System.out.println("Invalid login credentials");
            return;
        }
        Menu<String> coachMenu = new Menu<>("\nHELLO, COACH " + coachId, coachOptions) {
            @Override
            public void execute(int ch) throws ClassNotFoundException {
                switch (ch) {
                    case 1 -> courseService.add(coachId);
                    case 2 -> courseService.update();
                    case 3 -> courseService.delete();
                    case 4 -> courseService.display();
                    case 5 -> courseService.displayLearners();
                    case 6 -> workoutService.add();
                    case 7 -> workoutService.update(null);
                    case 8 -> workoutService.delete();
                    case 9 -> workoutService.display();
                }
            }
        };
        coachMenu.run();
    }
}
