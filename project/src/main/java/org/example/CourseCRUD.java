package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class CourseCRUD {

    public static Scanner scanner = new Scanner(System.in);

    public void courseCRUDMenu() {
        System.out.printf("""
                Menu
                ========
                0. Go back to main menu
                1. Add new course
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
                        System.out.println("Exiting");
                        inSubMenu = false;
                        break;
                    case 1: insertCourse();
                        break;
                    case 2: showAllCourses();
                        break;
                    case 3: updateCourse();
                        break;
                    case 4:deleteCourse();
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

    public void insertCourse() {
        Course course = new Course();

        System.out.println("Inter the course name");
        String courseName = scanner.nextLine();
        course.setCourseName(courseName);

        System.out.println("Inter the school Id");
        int schoolId = scanner.nextInt();

        JPAUtil.inTransaction(em -> {
            School school = em.find(School.class, schoolId);
            if (school != null) {
                course.setCourseSchoolId(school);

                em.persist(course);
                System.out.println("Course " + courseName + " added.");
            } else {
                System.out.println("School with id = " + schoolId + " not exists");
            }

        });

    }

    private void showAllCourses() {
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

    public void deleteCourse(){
        System.out.println("Enter the ID of the course to delete");
        int courseId = scanner.nextInt();

        JPAUtil.inTransaction(em -> {
            Course course = em.find(Course.class, courseId);
            if (course != null) {
                em.remove(course);

                System.out.println(Course.class.getSimpleName() + " with ID " + courseId + " deleted." );
            } else {
                System.out.println(Course.class.getSimpleName() + " with ID " + courseId + " not found.");
            }
        });
    }
}
