package view;

import repository.AdminRepo;
import service.CoachService;
import service.LearnerService;
import utils.Utils;

import java.util.Scanner;

public class AdminMenu {
    private static final String[] adminCoachOptions = {
            "Add new coach", "Update coach", "Delete coach", "Display coaches", "Add new learner", "Update learner", "Delete learner", "Display learners", "Logout"};
    static Scanner input = new Scanner(System.in);

    public static void displayAdminMenu() throws ClassNotFoundException {
        CoachService coachService = new CoachService();
        LearnerService learnerService = new LearnerService();
        AdminRepo adminRepo = new AdminRepo();

        String username = Utils.getString("Enter username: ", input);
        String password = Utils.getPassword("Enter password: ");
        if (!adminRepo.validateLogin(username, password)) {
            System.out.println("Invalid username or password");
            return;
        }

        Menu adminCoachMenu = new Menu("\nHELLO, ADMIN", adminCoachOptions) {
            @Override
            public void execute(int ch) throws ClassNotFoundException {
                switch (ch) {
                    case 1 -> coachService.add();
                    case 2 -> coachService.update(null);
                    case 3 -> coachService.delete();
                    case 4 -> coachService.display();
                    case 5 -> learnerService.add();
                    case 6 -> learnerService.update(null);
                    case 7 -> learnerService.delete(null);
                    case 8 -> learnerService.display();
                }
            }
        };
        adminCoachMenu.run();
    }
}
