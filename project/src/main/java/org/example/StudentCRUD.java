package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
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
                
                Menu
                ========
                0. Go back to main menu
                1. Add new student
                2. Show all students
                3. Update a student
                4. Delete a student
                
                """);

                System.out.println("Make a choice");
                int choice = scanner.nextInt();

                scanner.nextLine();
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

    public void showAllStudents(){
        EntityManager em = JPAUtil.getEntityManager();

        TypedQuery<Student> query = em.createQuery("SELECT s FROM Student s ", Student.class);

        List<Student> students = query.getResultList();
        students.forEach(System.out::println);

        em.close();
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
        System.out.println("Enter the ID of the student to delete");
        int studentID = scanner.nextInt();

        JPAUtil.inTransaction(em -> {
            Student student = em.find(Student.class, studentID);
            if (student != null) {
                em.remove(student);

                System.out.println(Student.class.getSimpleName() + " with ID " + studentID + " deleted." );
            } else {
                System.out.println(Student.class.getSimpleName() + " with ID " + studentID + " not found.");
            }
        });
    }

}
