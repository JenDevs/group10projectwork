package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Scanner;

public class ExamCRUD {

    public static Scanner scanner = new Scanner(System.in);

    public void examCRUDMenu(){

        boolean inSubMenu = true;
        while (inSubMenu) {
            try {
            System.out.print("""
                
                Menu
                ========
                0. Go back to main menu
                1. Add new exam
                2. Show all exams
                3. Update an exam
                4. Delete an exam
                
                """);

                System.out.println("Make a choice");
                String input = scanner.nextLine();
                int choice;

                try {
                    choice = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid choice, only numbers between 0 and 4 are accepted");
                    continue;
                }

                switch (choice) {
                    case 0:
                        inSubMenu = false;
                        break;
                    case 1: insertExam();
                        break;
                    case 2: showAllExam();
                        break;
                    case 3: updateExam();
                        break;
                    case 4: deleteExam();
                        break;
                    default:
                        System.out.println("Invalid choice, please try again");
                        break;
                }

            } catch (Exception e) {
                System.out.println("An unexpected error occurred" + e.getMessage());
            }
        }

    }

    public void insertExam() {
        Exam exam = new Exam();

        System.out.println("Inter the exam name");
        String examName = scanner.nextLine();
        exam.setExamName(examName);

        System.out.println("Which student would you like to insert?");
        String studentName = scanner.nextLine();

        JPAUtil.inTransaction(em -> {
            Student student = em.createQuery("SELECT s FROM Student s WHERE s.studentName = :studentName", Student.class)
                    .setParameter("studentName",studentName )
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if (student != null) {
                exam.setExamStudentId(student);
                System.out.println("What a rating " + studentName + " got");
                int examRating = scanner.nextInt();
                exam.setExamRating(examRating);
                em.persist(exam);
                System.out.println("Exam " + examName + " added to student " + studentName);
            } else {
                System.out.println("Student with name = " + studentName + " do not exists");
            }
        });
    }

    public void showAllExam(){
        EntityManager em = JPAUtil.getEntityManager();

        TypedQuery<Exam> query = em.createQuery("SELECT e FROM Exam e ", Exam.class);

        List<Exam> exams = query.getResultList();
        exams.forEach(System.out::println);

        em.close();
    }

    private void updateExam() {
        System.out.println("Which exam would you like to update?");
        String oldExamName = scanner.nextLine();

        JPAUtil.inTransaction(em -> {
            Exam exam = em.createQuery("SELECT e FROM Exam e WHERE e.examName = :examName", Exam.class )
                    .setParameter("examName", oldExamName)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            if (exam != null) {
                System.out.println("Enter a new name for the exam");
                String newExamName = scanner.nextLine();
                exam.setExamName(newExamName);
                System.out.println("Enter a new rating for the exam");
                int newExamRating = scanner.nextInt();
                exam.setExamRating(newExamRating);
                System.out.println(Exam.class.getSimpleName() + " with name " + oldExamName + " updated");
            } else {
                System.out.println(Exam
                        .class.getSimpleName() + " with name " + oldExamName + " not found");
            }
        });
    }

    public void deleteExam(){
        System.out.println("Enter the name of the exam you want to delete");
        String examName = scanner.nextLine();

        JPAUtil.inTransaction(em -> {
            Exam exam = em.createQuery("SELECT e FROM Exam e WHERE e.examName = :examName", Exam.class )
                    .setParameter("examName", examName)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            if (exam != null) {
                em.remove(exam);

                System.out.println(Exam.class.getSimpleName() + " with name " + examName + " deleted." );
            } else {
                System.out.println(Exam.class.getSimpleName() + " with name " + examName + " not found.");
            }
        });

    }

}
