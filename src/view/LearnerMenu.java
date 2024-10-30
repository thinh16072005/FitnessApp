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

    public static void displayLearnerMenu(String email) throws Exception {
        SubscriptionService subscriptionService = new SubscriptionService();
        SubscriptionRepo subscriptionRepo = new SubscriptionRepo();
        LearnerRepo learnerRepo = new LearnerRepo();
        LearnerService learnerService = new LearnerService();

        Menu coachMenu = new Menu("\nHELLO, LEARNER " + learnerRepo.getLearnerFirstName(email), learnerOptions) {
            @Override
            public void execute(int ch) throws ClassNotFoundException {
                switch (ch) {
                    case 1 -> {
                        String courseId = Utils.getString("Enter course ID: ", input);
                        subscriptionService.viewWeeklySubscription(email, subscriptionRepo.getCourseName(subscriptionRepo.getSubscriptionId(email, courseId)));
                    }
                    case 2 -> subscriptionService.register(email);
                    case 3 -> subscriptionService.unenroll();
                    case 4 -> learnerService.viewProfile(email);
                    case 5 -> learnerService.update(email, "Learner");
                    case 6 -> learnerService.updatePassword(email);
                }
            }
        };
        coachMenu.run();
    }
}
