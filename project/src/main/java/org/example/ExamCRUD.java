package org.example;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ExamCRUD {

    public static Scanner scanner = new Scanner(System.in);

    public void examCRUDMenu(){
        System.out.printf("""
                Menu
                ========
                0. Go back to main menu
                1. Add new exam
                2. Show all exams
                3. Update an exam
                4. Delete an exam
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
                    case 2: // visa
                        break;
                    case 3: // uppdatera
                        break;
                    case 4: deleteExam();
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

    // raderar en examen med hjälp av användarens inmatning av ID för examen.
    public void deleteExam(){
        System.out.println("Enter the ID of the exam to delete");
        int examID = scanner.nextInt();

        JPAUtil.inTransaction(em -> {
            Exam exam = em.find(Exam.class, examID);
            if (exam != null) {
                em.remove(exam);

                System.out.println(Exam.class.getSimpleName() + " with ID " + examID + " deleted." );
            } else {
                System.out.println(Exam.class.getSimpleName() + " with ID " + examID + " not found.");
            }
        });
    }

}
