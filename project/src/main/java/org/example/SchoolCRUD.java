package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Scanner;

public class SchoolCRUD {

    public static Scanner scanner = new Scanner(System.in);

    public void schoolCRUDMenu(){

        boolean inSubMenu = true;
        while (inSubMenu) {
            try {
        System.out.print("""
                
                School menu
                =======================
                0. Go back to main menu
                1. Add new school
                2. Show all schools
                3. Update a school
                4. Delete a school
                
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

            } catch (Exception e) {
                System.out.println("An unexpected error occurred" + e.getMessage());
            }
        }
    }

    public void insertSchool() {
        School school = new School();

        System.out.println("Enter the school name");
        String schoolName = scanner.nextLine();
        school.setSchoolName(schoolName);

        System.out.println("To which city would you like to add the school");
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
                System.out.println(schoolName + " added to " + cityName);
            } else {
                System.out.println("City with name " + cityName + " do not exists");
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
                System.out.println(School.class.getSimpleName() + " " + oldSchoolName + " updated to " + newSchoolName);
            } else {
                System.out.println(School
                        .class.getSimpleName() + " " + oldSchoolName + " not found");
            }
        });
    }

    public void deleteSchool(){
        System.out.println("Enter the name of the school you want to delete");
        String schoolName = scanner.nextLine();

        JPAUtil.inTransaction(em -> {
            School school = em.createQuery("SELECT s FROM School s WHERE s.schoolName = :schoolName", School.class )
                    .setParameter("schoolName", schoolName)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            if (school != null) {
                em.remove(school);

                System.out.println(School.class.getSimpleName() + " " + schoolName + " deleted." );
            } else {
                System.out.println(School.class.getSimpleName() + " " + schoolName + " not found.");
            }
        });
    }
}
