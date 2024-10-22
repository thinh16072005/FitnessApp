package view;

import repository.LearnerRepo;
import service.LearnerService;
import service.SubscriptionService;
import utils.Utils;

import java.util.Scanner;

public class LearnerMenu {
    public static String[] learnerOptions = {"View course", "Enroll a course", "Unenroll a course", "Change password", "Logout"};
    static Scanner input = new Scanner(System.in);

    public static void displayLearnerMenu() throws ClassNotFoundException {
        SubscriptionService subscriptionService = new SubscriptionService();
        LearnerRepo learnerRepo = new LearnerRepo();
        LearnerService learnerService = new LearnerService();

        String learnerId = Utils.getString("Enter learner ID: ", input);
        String password = Utils.getPassword("Enter password: ");

        if (!learnerRepo.validateLogin(learnerId, password)) {
            System.err.println("Invalid login credentials");
            return;
        }

        Menu<String> coachMenu = new Menu<>("\nHELLO, LEARNER " + learnerId, learnerOptions) {
            @Override
            public void execute(int ch) {
                switch (ch) {
                    case 1 -> subscriptionService.display();
                    case 2 -> subscriptionService.register(learnerId);
                    case 3 -> subscriptionService.unenroll();
                    case 4 -> learnerService.updatePassword(learnerId);
                }
            }
        };
        coachMenu.run();
    }
}
