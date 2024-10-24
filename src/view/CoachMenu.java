package view;

import repository.CoachRepo;
import service.CoachService;
import utils.Utils;

import java.util.Scanner;

public class CoachMenu {
    public static String[] coachOptions = {
            "Course Management",
            "Workout Management",
            "Exercise Management",
            "View profile",
            "Update profile",
            "Change password",
            "Exit"
            };
    static Scanner input = new Scanner(System.in);

    public static void displayCoachMenu() throws ClassNotFoundException {
        CoachRepo coachRepo = new CoachRepo();
        CoachSubMenus coachSubMenus = new CoachSubMenus();
        CoachService coachService = new CoachService();

        String coachId = Utils.getString("Enter coach ID: ", input);
        String password = Utils.getProperPassword("Enter password: ");

        if (!coachRepo.validateLogin(coachId, password)) {
            System.out.println("Invalid login credentials");
            return;
        }
        Menu<String> coachMenu = new Menu<>("\nHELLO, COACH " + coachId, coachOptions) {
            @Override
            public void execute(int ch) throws ClassNotFoundException {
                switch (ch) {
                    case 1 -> coachSubMenus.displayCourseMenu(coachId);
                    case 2 -> coachSubMenus.displayWorkoutMenu();
                    case 3 -> coachSubMenus.displayExerciseMenu();
                    case 4 -> coachService.viewProfile(coachId);
                    case 5 -> coachService.update(coachId);
                    case 6 -> coachService.updatePassword(coachId);
                    case 7 -> {}
                }
            }
        };
        coachMenu.run();
    }
}
