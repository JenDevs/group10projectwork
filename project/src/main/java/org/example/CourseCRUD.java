package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class CourseCRUD {

    public static Scanner scanner = new Scanner(System.in);

    public void courseCRUDMenu() {
        boolean inSubMenu = true;

        while (inSubMenu) {
            try {
                System.out.printf("""
                        Menu
                        ========
                        0. Go back to main menu
                        1. Add a course
                        2. Show all courses
                        3. Update a course
                        4. Delete a course
                        %n""");

                System.out.print("Make a choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 0:
                        inSubMenu = false;
                        break;
                    case 1:
                        addCourse();
                        break;
                    case 2:
                        showCourses();
                        break;
                    case 3:
                        updateCourse();
                        break;
                    case 4:
                        deleteCourse();
                        break;
                    default:
                        System.out.println("Invalid choice, please try again.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input, only integers are valid choices.");
                scanner.nextLine();
            }
        }
    }

    private void addCourse() {
        System.out.print("Enter course name: ");
        String name = scanner.nextLine();

        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            Course newCourse = new Course();
            newCourse.setCourseName(name);
            em.persist(newCourse);
            transaction.commit();
            System.out.println("Course added successfully!");
        } catch (Exception e) {
            transaction.rollback();
            System.out.println("Failed to add course: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    private void showCourses() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            List<Course> courses = em.createQuery("SELECT c FROM Course c", Course.class).getResultList();
            if (courses.isEmpty()) {
                System.out.println("No courses available.");
            } else {
                System.out.println("\nCourses List:");
                for (int i = 0; i < courses.size(); i++) {
                    System.out.println((i + 1) + ". " + courses.get(i).getCourseName());
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to retrieve courses: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    private void updateCourse() {
        System.out.print("Enter the course name you want to update: ");
        String oldCourseName = scanner.nextLine();

        JPAUtil.inTransaction(em -> {
            Course course = em.createQuery("SELECT c FROM Course c WHERE c.courseName = :courseName", Course.class)
                    .setParameter("courseName", oldCourseName)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if (course != null) {
                System.out.print("Enter the new name for the course: ");
                String newCourseName = scanner.nextLine();
                course.setCourseName(newCourseName);
                System.out.println("Course '" + oldCourseName + "' updated to '" + newCourseName + "'.");
            } else {
                System.out.println("Course with name '" + oldCourseName + "' not found.");
            }
        });
    }

    private void deleteCourse() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            List<Course> courses = em.createQuery("SELECT c FROM Course c", Course.class).getResultList();
            if (courses.isEmpty()) {
                System.out.println("No courses available to delete.");
                return;
            }

            System.out.println("Select a course to delete:");
            for (int i = 0; i < courses.size(); i++) {
                System.out.println((i + 1) + ". " + courses.get(i).getCourseName());
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice < 1 || choice > courses.size()) {
                System.out.println("Invalid choice, no course selected.");
                return;
            }
            Course courseToDelete = courses.get(choice - 1);

            EntityTransaction transaction = em.getTransaction();
            try {
                transaction.begin();
                em.remove(courseToDelete);
                transaction.commit();
                System.out.println("Course '" + courseToDelete.getCourseName() + "' deleted successfully.");
            } catch (Exception e) {
                transaction.rollback();
                System.out.println("Failed to delete course: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Failed to retrieve courses: " + e.getMessage());
        } finally {
            em.close();
        }
    }
}