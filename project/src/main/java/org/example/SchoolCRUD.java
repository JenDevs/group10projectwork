package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class SchoolCRUD {

    public static Scanner scanner = new Scanner(System.in);

    public void schoolCRUDMenu(){
        System.out.printf("""
                
                Menu
                ========
                0. Go back to main menu
                1. Add new school
                2. Show all schools
                3. Update a school
                4. Delete a school
                
                """);

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
                    case 1: insertSchool();
                        break;
                    case 2: showAllSchools();
                        break;
                    case 3: updateSchool();
                        break;
                    case 4: deleteSchool();
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

    public void insertSchool() {
        School school = new School();

        System.out.println("Inter the school name");
        String schoolName = scanner.nextLine();
        school.setSchoolName(schoolName);

        System.out.println("Which city would you like to insert?");
        String cityName = scanner.nextLine();

        JPAUtil.inTransaction(em -> {
            City city = em.createQuery("SELECT c FROM City c WHERE c.cityName = :cityName", City.class)
                    .setParameter("cityName",cityName )
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if (city != null) {
                school.setSchoolCityId(city);

                em.persist(school);
                System.out.println("School " + schoolName + " added to city " + cityName);
            } else {
                System.out.println("City with name = " + cityName + " do not exists");
            }
        });
    }

    public void showAllSchools(){
        EntityManager em = JPAUtil.getEntityManager();

        TypedQuery<School> query = em.createQuery("SELECT s FROM School s ", School.class);

        List<School> schools = query.getResultList();
        schools.forEach(System.out::println);

        em.close();
    }

    private void updateSchool() {
        System.out.println("Which school would you like to update?");
        String oldSchoolName = scanner.nextLine();

        JPAUtil.inTransaction(em -> {
            School school = em.createQuery("SELECT s FROM School s WHERE s.schoolName = :schoolName", School.class )
                    .setParameter("schoolName", oldSchoolName)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            if (school != null) {
                System.out.println("Enter a new name for the school");
                String newSchoolName = scanner.nextLine();
                school.setSchoolName(newSchoolName);
                System.out.println(School.class.getSimpleName() + " with name " + oldSchoolName + " updated");
            } else {
                System.out.println(School
                        .class.getSimpleName() + " with name " + oldSchoolName + " not found");
            }
        });
    }

    public void deleteSchool(){
        System.out.println("Enter the ID of the school to delete");
        int schoolID = scanner.nextInt();

        JPAUtil.inTransaction(em -> {
            School school = em.find(School.class, schoolID);
            if (school != null) {
                em.remove(school);

                System.out.println(School.class.getSimpleName() + " with ID " + schoolID + " deleted." );
            } else {
                System.out.println(School.class.getSimpleName() + " with ID " + schoolID + " not found.");
            }
        });
    }
}
