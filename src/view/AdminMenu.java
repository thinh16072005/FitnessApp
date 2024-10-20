package view;

import service.CoachService;

import java.util.Scanner;

public class AdminMenu {

    private static String[] adminOptions = {"Coach Admin", "Learner Admin"};
    private static String[] adminCoachOptions = {"Add new coach", "Update coach", "Delete coach", "Display coaches"};
    private static String[] adminLearnerOptions = {"Add new learner", "Update learner", "Delete learner", "Display learners"};
    Scanner input = new Scanner(System.in);

    public static void displayAdminMenu() throws ClassNotFoundException {
        CoachService coachService = new CoachService();

//        checkCoachIdExist();
        Menu adminCoachMenu = new Menu("\nHELLO, ADMIN FOR COACH", adminCoachOptions) {
            @Override
            public void execute(int ch) throws ClassNotFoundException {
                switch (ch) {
                    case 1 -> coachService.add();
                    case 2 -> coachService.update(null);
                    case 3 -> coachService.delete(null);
                    case 4 -> coachService.display();
                }
            }
        };
        adminCoachMenu.run();
    }
}
