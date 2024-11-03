package view;

import model.Slot;
import repository.LearnerRepo;
import repository.SlotRepo;
import repository.SubscriptionRepo;
import service.CourseService;
import service.LearnerService;
import service.SlotService;
import service.SubscriptionService;
import utils.Utils;

import java.util.Scanner;

public class LearnerMenu {
    public static String[] learnerOptions = {
            "View weekly schedule",
            "View slot information",
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
        CourseService courseService = new CourseService();
        SlotRepo slotRepo = new SlotRepo(); // Initialize or get an instance
        SlotService slotService = new SlotService(slotRepo); // Pass slotRepo to constructor
        Slot slot = new Slot();

        Menu coachMenu = new Menu("\nHELLO, LEARNER " + learnerRepo.getLearnerFirstName(email), learnerOptions) {
            @Override
            public void execute(int ch) throws ClassNotFoundException {
                switch (ch) {
                    case 1 -> {
                        courseService.display();
                        String courseId = Utils.getString("Enter course ID: ", input);
                        subscriptionService.viewWeeklySubscription(email, courseId);
                    }
                    case 2 -> {
                        String courseId = Utils.getString("Enter course ID: ", input);
                        slotService.printSlotDetails(slot.getSlotId());

                    }
                    case 3 -> subscriptionService.register(email);
                    case 4 -> subscriptionService.unenroll();
                    case 5 -> learnerService.viewProfile(email);
                    case 6 -> learnerService.update(email, "Learner");
                    case 7 -> learnerService.updatePassword(email);
                    case 8 -> System.out.println("Logging out...");
                    default -> System.out.println("Invalid option");
                }
            }
        };
        coachMenu.run();
    }
}
