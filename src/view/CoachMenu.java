package view;

import repository.CoachRepo;
import service.CoachService;

public class CoachMenu {
    public static String[] coachOptions = {
            "Course Management",
//            "Workout Management",
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
//                    case 2 -> coachSubMenus.displayWorkoutMenu();
                    case 2 -> coachSubMenus.displayExerciseMenu();
                    case 3 -> coachService.viewProfile(email);
                    case 4 -> coachService.update(email, "Coach");
                    case 5 -> coachService.updatePassword(email);
                    case 6 -> System.out.println("\nLogging out...");
                }
            }
        };
        coachMenu.run();
    }
}
