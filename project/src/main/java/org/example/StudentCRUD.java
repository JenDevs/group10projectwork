package org.example;

import java.util.InputMismatchException;
import java.util.Scanner;

public class StudentCRUD {

    public static Scanner scanner = new Scanner(System.in);

    public void studentCRUDMenu(){
        System.out.printf("""
                Menu
                ========
                0. Go back to main menu
                1. Add new student
                2. Show all students
                3. Update an student
                4. Delete an student
                %n""");

        boolean inSubMenu = true;
        while (inSubMenu) {
            try {
                System.out.println();
                System.out.println("Make a choice");
                int choice = scanner.nextInt();

                scanner.nextLine();
                switch (choice) {
                    case 0:
                        System.out.println("Exiting");
                        inSubMenu = false;
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                    default:
                        System.out.println("Invalid choice, please try again");
                        break;
                }

            } catch (InputMismatchException e) {
                System.out.println("Invalid input, only integers are valid choices");
            }
        }

    }

}

