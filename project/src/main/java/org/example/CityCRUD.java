package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class CityCRUD {

    public static Scanner scanner = new Scanner(System.in);

    public void cityCRUDMenu(){

        boolean inSubMenu = true;
        while (inSubMenu) {
            try {
                System.out.print("""
                
                City menu
                =======================
                0. Go back to main menu
                1. Add new city
                2. Show all cities
                3. Update a city
                4. Delete a city
                
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
                    case 1: cityInsert();
                        break;
                    case 2: citySelect();
                        break;
                    case 3: cityUpdate();
                        break;
                    case 4: deleteCity();
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

    public void cityInsert() {
        City city = new City();

        System.out.println("Enter the city name");
        String cityName = scanner.nextLine();
        city.setCityName(cityName);


        JPAUtil.inTransaction(entityManager -> entityManager.persist(city));

    }

    public void citySelect(){
        EntityManager em = JPAUtil.getEntityManager();

        TypedQuery<City> query = em.createQuery("SELECT c FROM City c ", City.class);

        List<City> cities = query.getResultList();
        cities.forEach(System.out::println);

        em.close();
    }

    public void cityUpdate () {
        System.out.println("Which city would you like to update?");
        String oldCityName = scanner.nextLine();

        JPAUtil.inTransaction(em -> {
            City city = em.createQuery("SELECT c FROM City c WHERE c.cityName = :cityName", City.class )
                    .setParameter("cityName", oldCityName)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            if (city != null) {
                System.out.println("Enter a new name for the city");
                String newCityName = scanner.nextLine();
                city.setCityName(newCityName);
                System.out.println(City.class.getSimpleName() + " " + oldCityName + " updated to "+ newCityName );
            } else {
                System.out.println(City.class.getSimpleName() + " with name " + oldCityName + " not found");
            }
        });
    }

    public void deleteCity(){
        System.out.println("Enter the name of the city you want to delete");
        String cityName = scanner.nextLine();

        JPAUtil.inTransaction(em -> {
            City city = em.createQuery("SELECT c FROM City c WHERE c.cityName = :cityName", City.class )
                    .setParameter("cityName", cityName)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            if (city != null) {
                em.remove(city);

                System.out.println(City.class.getSimpleName() + " " + cityName + " deleted." );
            } else {
                System.out.println(City.class.getSimpleName() + " with name " + cityName + " not found.");
            }
        });
    }

}
