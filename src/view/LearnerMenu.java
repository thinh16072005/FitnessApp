package view;

import repository.LearnerRepo;
import repository.SubscriptionRepo;
import service.LearnerService;
import service.SubscriptionService;
import utils.Utils;

import java.util.Scanner;

public class LearnerMenu {
    public static String[] learnerOptions = {
            "View weekly schedule",
            "Enroll a course",
            "Unenroll a course",
            "View profile",
            "Update profile",
            "Change password",
            "Logout"
    };
    static Scanner input = new Scanner(System.in);

    public static void displayLearnerMenu() throws ClassNotFoundException {
        SubscriptionService subscriptionService = new SubscriptionService();
        SubscriptionRepo subscriptionRepo = new SubscriptionRepo();
        LearnerRepo learnerRepo = new LearnerRepo();
        LearnerService learnerService = new LearnerService();

        String learnerId = Utils.getString("Enter learner ID: ", input);
        String password = Utils.getProperPassword("Enter password: ");

        if (!learnerRepo.validateLogin(learnerId, password)) {
            System.err.println("Invalid login credentials");
            return;
        }

        Menu<String> coachMenu = new Menu<>("\nHELLO, LEARNER " + learnerId, learnerOptions) {
            @Override
            public void execute(int ch) {
                switch (ch) {
                    case 1 -> {
                        String courseId = Utils.getString("Enter course ID: ", input);
                        subscriptionService.viewWeeklySubscription(learnerId, subscriptionRepo.getCourseName(subscriptionRepo.getSubscriptionId(learnerId, courseId)));
                    }
                    case 2 -> subscriptionService.register(learnerId);
                    case 3 -> subscriptionService.unenroll();
                    case 4 -> learnerService.viewProfile(learnerId);
                    case 5 -> learnerService.update(learnerId);
                    case 6 -> learnerService.updatePassword(learnerId);
                }
            }
        };
        coachMenu.run();
    }
}
