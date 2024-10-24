package view;

import repository.AdminRepo;
import service.CoachService;
import service.LearnerService;
import utils.Utils;

import java.util.Scanner;

public class AdminMenu {
    private static final String[] adminCoachOptions = {
            "Add new admin",
            "Add new coach",
            "Delete coach",
            "Display coaches",
            "Add new learner",
            "Delete learner",
            "Display learners",
            "Logout"};
    static Scanner input = new Scanner(System.in);

    public static void displayAdminMenu() throws ClassNotFoundException {
        CoachService coachService = new CoachService();
        LearnerService learnerService = new LearnerService();
        AdminRepo adminRepo = new AdminRepo();

        String username = Utils.getString("Enter username: ", input);
        String password = Utils.getProperPassword("Enter password: ");

        if (!adminRepo.validateLogin(username, password)) {
            System.err.println("Invalid username or password");
            return;
        }

        Menu<String> adminCoachMenu = new Menu<>("\nHELLO, ADMIN", adminCoachOptions) {
            @Override
            public void execute(int ch) {
                switch (ch) {
                    case 1 -> adminRepo.createAdmin();
                    case 2 -> coachService.add();
                    case 3 -> coachService.delete();
                    case 4 -> coachService.display();
                    case 5 -> learnerService.add();
                    case 6 -> learnerService.delete();
                    case 7 -> learnerService.display();
                }
            }
        };
        adminCoachMenu.run();
    }
}
