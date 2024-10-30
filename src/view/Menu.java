package view;


import java.util.ArrayList;
import java.util.Scanner;

public abstract class Menu {
    private String title;
    private ArrayList<String> choices;

    public Menu() {
    }

    public Menu(String title, String[] mchon) {
        this.title = title;
        choices= new ArrayList<>();
        for(String m:mchon) choices.add(m);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public ArrayList<String> getChoices()
    {
        return choices;
    }

    public void setChoices(ArrayList<String> choices) {
        this.choices = choices;
    }
    //----------------------------------------------------
    public void displayMenu() {
        System.out.println(title);
        System.out.println("-------------------");
        for(int i = 0; i < choices.size(); i++){
            System.out.println((i + 1)+". " + choices.get(i));
        }
        System.out.println("-------------------");
    }
    //----------------------------------------------------
    public int getSelected()
    {
        displayMenu();
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter your choice: ");
        return sc.nextInt();
    }
    //----------------------------------------------------
    public abstract void execute(int ch) throws Exception;
    //----------------------------------------------------
    public void run() throws Exception
    {
        while (true) {
            int ch = getSelected();
            execute(ch);
            if (ch == choices.size()) {
                break;
            }
        }
    }
}
//----------------------------------------------------