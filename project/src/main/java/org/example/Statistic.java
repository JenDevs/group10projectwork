package org.example;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Statistic {
    public static Scanner scanner = new Scanner(System.in);

    public void showStatisticMenu(){

        boolean inSubMenu = true;
        while (inSubMenu) {
            try {
                System.out.print("""
                
                Menu
                ========
                0. Go back to main menu
                1. Show top 3 Students
                2. Highest and lowest grades
                   for every course
                
                """);
                System.out.println("Make a choice");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 0:
                        inSubMenu = false;
                        break;
                    case 1: topThreeStudents();
                        break;
                    case 2: gradesForCourses();
                        break;
                    default:
                        System.out.println("Invalid choice, please try again");
                        break;
                }

            } catch (InputMismatchException e) {
                System.out.println("Invalid input, only integers are valid choices");
                scanner.nextLine();
            }
        }
    }

    private void gradesForCourses() {

    }

    private void topThreeStudents() {

    }
}
