package view;

import repository.CoachRepo;
import service.CoachService;

public class CoachMenu {
    public static String[] coachOptions = {
            "Course Management",
            "Workout Management",
            "Exercise Management",
            "View profile",
            "Update profile",
            "Change password",
            "Logout"
            };

    public static void displayCoachMenu(String email) throws Exception {
        CoachRepo coachRepo = new CoachRepo();
        CoachSubMenus coachSubMenus = new CoachSubMenus();
        CoachService coachService = new CoachService();

        Menu coachMenu = new Menu("\nHELLO, COACH " + coachRepo.getCoachFirstName(email), coachOptions) {
            @Override
            public void execute(int ch) throws Exception {
                switch (ch) {
                    case 1 -> coachSubMenus.displayCourseMenu(email);
                    case 2 -> coachSubMenus.displayWorkoutMenu();
                    case 3 -> coachSubMenus.displayExerciseMenu();
                    case 4 -> coachService.viewProfile(email);
                    case 5 -> coachService.update(email, "Coach");
                    case 6 -> coachService.updatePassword(email);
                    case 7 -> System.out.println("\nLogging out...");
                }
            }
        };
        coachMenu.run();
    }
}
