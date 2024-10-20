package view;

import repository.CoachRepo;
import service.CourseService;
import utils.Utils;

public class CoachMenu {
    public static String[] coachOptions = {"Add new course", "Update course", "Delete course", "Display courses"};

    public static void displayCoachMenu() throws ClassNotFoundException {
        CourseService courseService = new CourseService();
        CoachRepo coachRepo = new CoachRepo();

        String coachId = Utils.getProperString("Enter coach ID: ");
        if (!coachRepo.checkCoachIdExist(coachId)) {
            System.out.println("Coach ID does not exist!");
            return;
        }
        Menu<String> coachMenu = new Menu<>("\nHELLO, COACH " + coachId, coachOptions) {
            @Override
            public void execute(int ch) throws ClassNotFoundException {
                switch (ch) {
                    case 1 -> courseService.add(null, coachId);
                    case 2 -> courseService.update(null);
                    case 3 -> courseService.delete(null);
                }
            }
        };
        coachMenu.run();
    }
}
