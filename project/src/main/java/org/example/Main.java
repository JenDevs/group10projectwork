package org.example;

import jakarta.persistence.EntityManager;


public class Main {
    public static void main(String[] args) {

        EntityManager em = JPAUtil.getEntityManager();


        Country country = new Country();
        country.setCountryName("Poland");
        country.setCountryCode("pl");

        JPAUtil.inTransaction(entityManager ->  {
            entityManager.persist(country);
        });

        // delete
        JPAUtil.inTransaction(entityManager -> {
            Country poland = entityManager.find(Country.class, "pl");
            if (poland != null)
                entityManager.remove(poland);
        });


    }
}


