package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class CityCRUD {

    public static Scanner scanner = new Scanner(System.in);

    // meny för att välja vilken CRUD, menyn kan användas för alla tabeller, genom att
    public void cityCRUDMenu(){

        System.out.printf("""
                Menu
                ========
                0. Go back to main menu
                1. Add new city
                2. Show all cities
                3. Update a city
                4. Delete a city
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
                    case 1: cityInsert();
                        break;
                    case 2:citySelect();
                        break;
                    case 3: updateCity();
                        break;
                    case 4: deleteCity();
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

    public void cityInsert() {
        City city = new City();
        System.out.println("Inter the city name");
        String cityName = scanner.nextLine();
        city.setCityName(cityName);
        int cityId = scanner.nextInt();
        city.setCityId(cityId);

        JPAUtil.inTransaction(entityManager ->  {
            entityManager.persist(city);
        });

    }



    public void citySelect(){
        EntityManager em = JPAUtil.getEntityManager();

        TypedQuery<City> query = em.createQuery("SELECT c FROM City c ", City.class);

        List<City> cytis = query.getResultList();
        cytis.forEach(System.out::println);

        em.close();

    }

    private void updateCity() {

    }


    // Raderar stad med ID.
    public void deleteCity(){
        System.out.println("Enter the ID of the city to delete");
        int cityID = scanner.nextInt();

        JPAUtil.inTransaction(em -> {
            City city = em.find(City.class, cityID);
            if (city != null) {
                em.remove(city);

                System.out.println(City.class.getSimpleName() + " with ID " + cityID + " deleted." );
            } else {
                System.out.println(City.class.getSimpleName() + " with ID " + cityID + " not found.");
            }
        });
    }
}
