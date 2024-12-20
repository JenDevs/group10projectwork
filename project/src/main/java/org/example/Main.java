package org.example;

import jakarta.persistence.EntityManager;

import java.util.InputMismatchException;
import java.util.Scanner;


public class Main {

    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        Menu menu = new Menu();
        CityCRUD cityMenu = new CityCRUD();
        CourseCRUD courseMenu = new CourseCRUD();
        ExamCRUD examMenu = new ExamCRUD();
        SchoolCRUD schoolMenu = new SchoolCRUD();
        StudentCRUD studentMenu = new StudentCRUD();
        Statistic statisticMenu = new Statistic();

        boolean isRunning = true;
        while (isRunning) {
            try {
                menu.showMainMenu();
                System.out.println("Make a choice");
                int choice = scanner.nextInt();

                scanner.nextLine();
                switch (choice) {
                    case 0:
                        System.out.println("Exiting");
                        isRunning = false;
                        break;
                    case 1:
                        cityMenu.cityCRUDMenu();
                        break;
                    case 2:
                        schoolMenu.schoolCRUDMenu();
                        break;
                    case 3:
                        courseMenu.courseCRUDMenu();
                        break;
                    case 4:
                        studentMenu.studentCRUDMenu();
                        break;
                    case 5:
                        examMenu.examCRUDMenu();
                        break;
                    case 6:
                        statisticMenu.showStatisticMenu();
                        break;
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

