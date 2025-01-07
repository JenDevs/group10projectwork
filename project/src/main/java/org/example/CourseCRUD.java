package org.example;

import jakarta.persistence.EntityManager;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class CourseCRUD {

    public static Scanner scanner = new Scanner(System.in);

    public void courseCRUDMenu() {

        boolean inSubMenu = true;
        while (inSubMenu) {
            try {

        System.out.print("""
                
                Menu
                ========
                0. Go back to main menu
                1. Add new course
                2. Show all courses
                3. Update a course
                4. Delete a course
                
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
                    default:
                        System.out.println("Invalid choice, please try again");
                        break;
                }

            } catch (Exception e) {
                System.out.println("An unexpected error occurred" + e.getMessage());
            }
        }
    }

    public void insertCourse() {
        Course course = new Course();

        System.out.println("Enter the course name");
        String courseName = scanner.nextLine();
        course.setCourseName(courseName);

        System.out.println("To which school would you like to add the course?");
        String schoolName = scanner.nextLine();

        JPAUtil.inTransaction(em -> {
            School school = em.createQuery("SELECT s FROM School s WHERE s.schoolName = :schoolName", School.class)
                    .setParameter("schoolName", schoolName)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if (school != null) {
                course.setCourseSchoolId(school);

                em.persist(course);
                System.out.println(courseName + " added to " + schoolName);
            } else {
                System.out.println("School with name " + schoolName + " do not exists");
            }

        });
    }

    private void showAllCourses() {
        try (EntityManager em = JPAUtil.getEntityManager()) {
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
                System.out.println("Course " + oldCourseName + " updated to " + newCourseName);
            } else {
                System.out.println("Course with name " + oldCourseName + " not found.");
            }
        });
    }

    public void deleteCourse(){
        System.out.println("Enter the name of the course to delete");
        String courseName = scanner.nextLine();

        JPAUtil.inTransaction(em -> {
            Course course = em.createQuery("SELECT c FROM Course c WHERE c.courseName = :courseName", Course.class)
                    .setParameter("courseName", courseName)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if (course != null) {
                em.remove(course);

                System.out.println(Course.class.getSimpleName() + " " + courseName + " deleted." );
            } else {
                System.out.println(Course.class.getSimpleName() + " " + courseName + " not found.");
            }
        });
    }
}
