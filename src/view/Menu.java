package view;

import java.util.ArrayList;
import java.util.Scanner;

public abstract class Menu<T> {
    protected String title;
    protected ArrayList<T> mChon;
    
    public static final Scanner input1 = new Scanner(System.in);
    public Menu(){}
    
    public Menu(String td, String[] mc){
        title=td;
        mChon= new ArrayList<>();
        for(String s:mc) mChon.add((T) s);
    }
//-------------------------------------------
    public void display(){
        System.out.println(title);
        System.out.println("--------------------------------");
        for(int i=0; i<mChon.size();i++)
            System.out.println((i+1)+"."+mChon.get(i));
        System.out.println("--------------------------------");
    }
//-------------------------------------------
    public int getSelected(){
        display();
        Scanner sc= new Scanner(System.in);
        System.out.print("Enter selection..");
        return sc.nextInt();
    }
//-------------------------------------------
    public abstract void execute(int n) throws ClassNotFoundException;
//-------------------------------------------
    public void run() throws ClassNotFoundException {
        while(true){
            int n=getSelected();
            if(n<=mChon.size())execute(n);
            else break;
        }
    }
//-------------------------------------------    
    public void runSubMenu() {
            while (true) {
                display();
                int choice = input1.nextInt();
                if (choice == mChon.size()) {
                    System.out.println("Returning to Main Menu...");
                    return;
                }
                executeSubMenu(choice);
            }
        }
    
    private void executeSubMenu(int choice) {
            if (choice < 1 || choice > mChon.size()) {
                System.out.println("Invalid choice!");
            } else {
                System.out.println("You selected: " + mChon.get(choice - 1));
                // Here you can add the specific logic for each submenu option
            }
        }
    
}
