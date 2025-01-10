package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class StudentCRUD {

    public static Scanner scanner = new Scanner(System.in);

    public void studentCRUDMenu(){

        boolean inSubMenu = true;
        while (inSubMenu) {
                try {

            System.out.print(
                """
                
                Student menu
                =======================
                0. Go back to main menu
                1. Add new student
                2. Show all students
                3. Update a student
                4. Delete a student
                
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
                    case 1: insertStudent();
                        break;
                    case 2: showAllStudents();
                        break;
                    case 3: updateStudent();
                        break;
                    case 4:deleteStudent();
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

    public void insertStudent() {
        Student student = new Student();

        System.out.println("Inter the student name");
        String studentName = scanner.nextLine();
        student.setStudentName(studentName);

        System.out.println("Enter the student birth date in format YYYY-MM-DD");
        String studentBirthDate = scanner.nextLine();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthDate = LocalDate.parse(studentBirthDate, formatter);
        student.setStudentBirthday(birthDate);

        System.out.println("Enter the student gender");
        String studentGender = scanner.nextLine();
        student.setStudentGender(studentGender);

        System.out.println("Enter the course where you would like to add the student");
        String courseName = scanner.nextLine();

        JPAUtil.inTransaction(em -> {
            Course course = em.createQuery("SELECT c FROM Course c WHERE c.courseName = :courseName", Course.class)
                    .setParameter("courseName",courseName )
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if (course != null) {
                student.setStudentCourseId(course);
                em.persist(student);
                System.out.println("Student " + studentName + " added to " + courseName);
            } else {
                System.out.println("Course with name = " + courseName + " do not exists");
            }
        });
    }

    public void showAllStudents2(){
        EntityManager em = JPAUtil.getEntityManager();

        TypedQuery<Student> query = em.createQuery("SELECT s FROM Student s ", Student.class);

        List<Student> students = query.getResultList();

        String format = "%-20s %-20s %-10s%n";
        System.out.printf(format, "Name", "Birthday", "Gender");
        System.out.println("--------------------------------------------------");

        for (Student student : students) {
            System.out.printf(format, student.getStudentName(), student.getStudentBirthday(), student.getStudentGender());
        }

        em.close();
    }
    public void showAllStudents() {
        EntityManager em = JPAUtil.getEntityManager();

        TypedQuery<Student> query = em.createQuery("SELECT s FROM Student s", Student.class);

        List<Student> students = query.getResultList();

        String format = "%-20s %-10s %-10s%n";
        System.out.printf(format, "Name", "Age", "Gender");
        System.out.println("---------------------------------------");

        for (Student student : students) {
            int age = calculateAge(student.getStudentBirthday());
            System.out.printf(format, student.getStudentName(), age, student.getStudentGender());
        }

        em.close();
    }

    private int calculateAge(LocalDate birthDate) {
        if (birthDate == null) {
            return 0;
        }
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    private void updateStudent() {
        System.out.println("Which student would you like to update?");
        String oldStudentName = scanner.nextLine();

        JPAUtil.inTransaction(em -> {
            Student student = em.createQuery("SELECT s FROM Student s WHERE s.studentName = :studentName", Student.class )
                    .setParameter("studentName", oldStudentName)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            if (student != null) {
                System.out.println("Enter a name for the student");
                String newStudentName = scanner.nextLine();
                student.setStudentName(newStudentName);
                System.out.println("Enter the gender for the student");
                String newGender = scanner.nextLine();
                student.setStudentGender(newGender);
                System.out.println(Student.class.getSimpleName() + " with name " + oldStudentName + " updated");
            } else {
                System.out.println(Student
                        .class.getSimpleName() + " with name " + oldStudentName + " not found");
            }
        });
    }


    public void deleteStudent(){
        System.out.println("Enter the name of the student you want to delete");
        String studentName = scanner.nextLine();

        JPAUtil.inTransaction(em -> {
        Student student = em.createQuery("SELECT s FROM Student s WHERE s.studentName = :studentName", Student.class)
                .setParameter("studentName", studentName)
                .getResultStream()
                .findFirst()
                .orElse(null);

            if (student != null) {
                em.remove(student);

                System.out.println(Student.class.getSimpleName() + " " + studentName + " deleted." );
            } else {
                System.out.println(Student.class.getSimpleName() + " " + studentName + " not found.");
            }
        });
    }

}
