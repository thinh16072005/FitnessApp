package controller;

import view.AdminMenu;
import view.CoachMenu;
import view.LearnerMenu;
import view.Menu;

public class Main extends Menu<String>{
    static String[] mainMenu = {"Coach", "Learner", "Admin", "Exit"};

    public Main(String title, String[] options) {
        super(title, options);
    }

    @Override
    public void execute(int n) throws ClassNotFoundException {
        switch (n) {
            case 1 -> CoachMenu.displayCoachMenu();
            case 2 -> LearnerMenu.displayLearnerMenu();
            case 3 -> AdminMenu.displayAdminMenu();
            case 4 -> System.exit(0);
        }
    }

    public static void main(String[] args) throws ClassNotFoundException {
        Main mainApp = new Main("FITNESS APP", mainMenu);
        mainApp.run();
    }
}
