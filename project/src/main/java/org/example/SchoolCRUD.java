package org.example;

import java.util.InputMismatchException;
import java.util.Scanner;

public class SchoolCRUD {

    public static Scanner scanner = new Scanner(System.in);

    public void schoolCRUDMenu(){
        System.out.printf("""
                Menu
                ========
                0. Go back to main menu
                1. Add new school
                2. Show all schools
                3. Update a school
                4. Delete a school
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

