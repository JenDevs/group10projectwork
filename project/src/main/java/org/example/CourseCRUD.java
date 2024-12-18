package org.example;

import java.util.InputMismatchException;
import java.util.Scanner;

public class CourseCRUD {

    public static Scanner scanner = new Scanner(System.in);

    public void courseCRUDMenu(){
        System.out.printf("""
                Menu
                ========
                0. Go back to main menu
                1. Add a course
                2. Show all courses
                3. Update a course
                4. Delete a course
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

