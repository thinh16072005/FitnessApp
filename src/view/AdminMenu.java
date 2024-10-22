package view;

import repository.AdminRepo;
import service.CoachService;
import service.LearnerService;
import utils.Utils;

import java.util.Scanner;

public class AdminMenu {
    private static final String[] adminCoachOptions = {
            "Add new admin", "Add new coach", "Update coach", "Delete coach", "Display coaches", "Add new learner", "Update learner", "Delete learner", "Display learners", "Logout"};
    static Scanner input = new Scanner(System.in);

    public static void displayAdminMenu() throws ClassNotFoundException {
        CoachService coachService = new CoachService();
        LearnerService learnerService = new LearnerService();
        AdminRepo adminRepo = new AdminRepo();

        String username = Utils.getString("Enter username: ", input);
   //     String password = PasswordEncryption.hashPassword((Utils.getProperPassword("Enter password: ")));
        String password = Utils.getProperPassword("Enter password: ");

        if (!adminRepo.validateLogin(username, password)) {
            System.err.println("Invalid username or password");
            return;
        }

        Menu adminCoachMenu = new Menu("\nHELLO, ADMIN", adminCoachOptions) {
            @Override
            public void execute(int ch) throws ClassNotFoundException {
                switch (ch) {
                    case 1 -> adminRepo.createAdmin();
                    case 2 -> coachService.add();
                    case 3 -> coachService.update();
                    case 4 -> coachService.delete();
                    case 5 -> coachService.display();
                    case 6 -> learnerService.add();
                    case 7 -> learnerService.update();
                    case 8 -> learnerService.delete();
                    case 9 -> learnerService.display();
                }
            }
        };
        adminCoachMenu.run();
    }
}
