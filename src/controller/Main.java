package controller;

import repository.SlotRepo;
import service.SlotService;
import view.*;

public class Main extends Menu {
    static String[] loginOptions = {"Login", "Register", "Exit"};

    public Main(String title, String[] options) {


        super(title, options);
    }

    @Override
    public void execute(int n) throws Exception {
        switch (n) {
            case 1 -> LoginRegisterMenu.displayLogin();
            case 2 -> LoginRegisterMenu.displayRegister();
            case 3 -> System.exit(0);
            default -> System.out.println("Invalid option");
        }
    }

    public static void main(String[] args) throws Exception {
        Main mainApp = new Main("WELCOME TO THE FITNESS APP", loginOptions);
        mainApp.run();
    }
}
