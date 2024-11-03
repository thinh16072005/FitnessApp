package view;

import repository.UserRepo;
import service.CoachService;
import service.LearnerService;
import utils.Utils;

public class LoginRegisterMenu {
    static UserRepo userRepo = new UserRepo();

    public static void displayLogin() throws Exception {
        String email = Utils.getValidEmail("Enter email: ");
        String password = Utils.getProperPassword("Enter password: ");

        boolean isLearner = userRepo.validateLoginByEmail(email, password, "learner");
        boolean isCoach = userRepo.validateLoginByEmail(email, password, "coach");

        if (isLearner) {
            LearnerMenu.displayLearnerMenu(email);
        } else if (isCoach) {
            CoachMenu.displayCoachMenu(email);
        } else {
            System.err.println("Invalid login credentials");
        }
    }

    static String[] registerOptions = {"Learner", "Coach", "Back"};

    public static void displayRegister() throws Exception {
        LearnerService learnerService = new LearnerService();
        CoachService coachService = new CoachService();
        Menu registerMenu = new Menu("\nWHO ARE YOU? ", registerOptions) {
            @Override
            public void execute(int ch) throws ClassNotFoundException {
                switch (ch) {
                    case 1 -> learnerService.add();
                    case 2 -> coachService.add();
                    case 3 -> System.out.println("Returning to main menu...");
                    default -> System.out.println("Invalid choice");
                }
            }
        };
        registerMenu.run();
    }
}
