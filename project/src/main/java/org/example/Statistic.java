package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Statistic {
    public static Scanner scanner = new Scanner(System.in);

    public void showStatisticMenu() {

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
                        3. Show courses by school
                        
                        """);
                System.out.println("Make a choice");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 0:
                        inSubMenu = false;
                        break;
                    case 1:
                        topThreeStudents();
                        break;
                    case 2:
                        gradesForCourses();
                        break;
                    case 3:
                        showCoursesBySchool();
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
        EntityManager em = JPAUtil.getEntityManager();

        String sql = """
                SELECT c.courseName,
                MAX(e.examRating) AS highestRating,
                MIN(e.examRating) AS lowestRating
                FROM Course c
                JOIN c.students s
                JOIN s.exams e
                GROUP BY c.courseName
       """;


        List<Object[]> results = em.createQuery(sql, Object[].class).getResultList();

        System.out.println("\nHighest and Lowest grades per Course:");
        for (Object[] result : results) {
            String courseName = (String) result[0];
            Integer highestRating = (Integer) result[1];
            Integer lowestRating = (Integer) result[2];

            System.out.println("\nCourse: " + courseName +
                    "\nHighest Grade: " + highestRating +
                    "\nLowest Grade: " + lowestRating);
        }
    }

    private void topThreeStudents() {
        EntityManager em = JPAUtil.getEntityManager();
        String sql = """
                    SELECT s.studentName, AVG(e.examRating) AS avgRating
                        FROM Exam e
                        JOIN e.examStudentId s
                        GROUP BY s.studentName
                        ORDER BY avgRating DESC
                """;

        List<Object[]> results = em.createQuery(sql, Object[].class)
                .setMaxResults(3)
                .getResultList();

        System.out.println("\nTop 3 Students:");
        for (Object[] result : results) {
            String studentName = (String) result[0];
            Double examRating = (Double) result[1];
            System.out.println("\nName: " + studentName + "\nAverage rating: " + examRating);
        }

    }

    private void showCoursesBySchool () {
        EntityManager em = JPAUtil.getEntityManager();

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the name of the school");
        String nameOfSchool = scanner.nextLine();

        String sql = """
                SELECT c.courseName
                FROM Course c
                JOIN c.courseSchoolId s
                WHERE s.schoolName = :schoolName
                ORDER BY c.courseName
                """;

        TypedQuery<String> query = em.createQuery(sql, String.class);
        query.setParameter("schoolName", nameOfSchool);

        List<String> courses = query.getResultList();

        System.out.println("Courses for " + nameOfSchool + ":");
        if (courses.isEmpty()) {
            System.out.println("No courses found");
        }
        else {
            courses.forEach(System.out::println);
        }

    }

}
